package net.maitland.quest.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.maitland.quest.model.attribute.*;
import net.maitland.quest.parser.sax.SaxQuestParser;
import net.maitland.quest.player.ConsolePlayer;
import org.junit.Test;

import java.io.InputStream;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Created by David on 03/02/2017.
 */
public class ChanceQuestIT {

    public void testFindDoor() {

        RandomFunctionAttribute.value = "10";

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            // enter quest
            GameStation station = sut.getNextStation(game, 0);

            // go north card
            station = sut.getNextStation(game, 1);

            assertEquals("Dynamic choice stationId is incorrect.", "go south", station.getChoices().get(1).getStationId());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGoNorth() {

        RandomFunctionAttribute.value = "2";

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            // enter quest
            GameStation station = sut.getNextStation(game, 0);

            // go north card
            station = sut.getNextStation(game, 1);

            // go north station
            station = sut.getNextStation(game, 1);

            assertTrue("Movement card 2 doesn't go north.", station.getChoices().get(0).getText().trim().equals("You follow the path north."));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testMonsterFight() {

        RandomFunctionAttribute.value = "7";

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            // enter quest
            GameStation station = sut.getNextStation(game, 0);

            // hear growl
            station = sut.getNextStation(game, 1);

            // monster chosen
            station = sut.getNextStation(game, 1);

            // start fight
            station = sut.getNextStation(game, 1);

            // go through 7 hits & status checks
            for (int i = 0; i < 7 * 2; i++) {
                station = sut.getNextStation(game, 1);
            }

            assertTrue("Player not dead after 7 hits.", station.getText().contains("You have been defeated."));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPoleOnPortcullis() {

        RandomFunctionAttribute.value = "7";

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            RandomFunctionAttribute randomFn = new RandomFunctionAttribute();
            randomFn.setValue("7");
            game.put(randomFn);

            // enter quest
            GameStation station = sut.getNextStation(game, 0);

            // hear growl
            station = sut.getNextStation(game, 1);

            // monster chosen
            station = sut.getNextStation(game, 1);

            // start fight
            station = sut.getNextStation(game, 1);

            // make killing blow
            randomFn.setValue("6");
            station = sut.getNextStation(game, 1);

            // check they're dead
            station = sut.getNextStation(game, 1);

            // pick up pole
            randomFn.setValue("6");
            station = sut.getNextStation(game, 1);

            // encounter trap
            randomFn.setValue("1");
            station = sut.getNextStation(game, 1);

            // face the portcullis
            randomFn.setValue("5");
            station = sut.getNextStation(game, 1);

            // jam portcullis with pole
            station = sut.getNextStation(game, 1);

            // check we're not blocked
            assertTrue("Player blocked by portcullis even though they had the pole.", !station.getText().contains("The portcullis completely blocks your path"));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUseLantern() {

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            NumberAttribute lightCardsLeftCounter = new NumberAttribute("light cards left counter", "0");
            StateAttribute hasLantern = new StateAttribute("has lantern", "true");
            StateAttribute hasOil = new StateAttribute("has oil", "true");

            // enter quest
            GameStation station = sut.getNextStation(game, 0);

            // adjust state
            Map<String, Attribute> attributes = game.copyAttributes();
            attributes.put(lightCardsLeftCounter.getName(), lightCardsLeftCounter);
            attributes.put(hasLantern.getName(), hasLantern);
            attributes.put(hasOil.getName(), hasOil);
            game.setAttributes(attributes);

            // take movement card
            station = sut.getNextStation(game, 1);

            assertTrue("Player did not use lantern visiting station '" + station.getId() + "': " + station.getText(), station.getText().contains("lantern with oil"));

            // complete visit
            station = sut.getNextStation(game, 1);

            assertTrue("Player still has oil", "false".equals(game.getAttributeValue("has oil")));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGameSize() {

        try {
            Quest quest = getQuest();
            Game game = quest.newGameInstance();
            ObjectMapper mapper = new ObjectMapper();
            String gameData = mapper.writeValueAsString(game);
            int gameDataSize = gameData.getBytes().length;
            assertTrue("Serialised game too big: " + gameDataSize, gameDataSize < 24576);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    protected Quest getQuest() throws Exception {
        Quest q = null;
        InputStream is = null;

        try {
            is = ConsolePlayer.class.getClassLoader().getResourceAsStream("chance-quest.xml");
            SaxQuestParser qp = new SaxQuestParser();
            q = qp.parseQuest(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return q;
    }
}
