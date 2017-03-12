package net.maitland.quest.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.maitland.quest.model.attribute.NumberAttribute;
import net.maitland.quest.model.attribute.RandomFunctionAttribute;
import net.maitland.quest.model.attribute.StateAttribute;
import net.maitland.quest.model.attribute.StringAttribute;
import net.maitland.quest.parser.sax.SaxQuestParser;
import net.maitland.quest.player.ConsolePlayer;
import org.junit.Test;

import java.io.InputStream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Created by David on 03/02/2017.
 */
public class ChanceQuestIT {

    public void testFindDoor() {

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            RandomFunctionAttribute randomFn = new RandomFunctionAttribute();
            randomFn.setValue("10");
            game.put(randomFn);

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

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            RandomFunctionAttribute randomFn = new RandomFunctionAttribute();
            randomFn.setValue("2");
            game.put(randomFn);

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
            game.put(lightCardsLeftCounter);
            game.put(hasLantern);
            game.put(hasOil);

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
