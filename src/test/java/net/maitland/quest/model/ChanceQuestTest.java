package net.maitland.quest.model;

import net.maitland.quest.SaxQuestParser;
import net.maitland.quest.player.ConsolePlayer;
import net.maitland.quest.player.ExpressionEvaluator;
import net.maitland.quest.player.QuestStateStation;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Created by David on 03/02/2017.
 */
public class ChanceQuestTest {

    @Test
    public void testMonsterFight() {

        try {
            Quest sut = getQuest();
            GameInstance game = sut.newGameInstance();

            Map<String, String> attributes = game.getCurrentState().copyAttributes();
            attributes.put("{random 1, 13}", "7");
            game.getCurrentState().setAttributes(attributes);

            // enter quest
            QuestStateStation station = getQuest().getNextStation(game, 0);

            // hear growl
            station = getQuest().getNextStation(game, 1);

            // start fight
            station = getQuest().getNextStation(game, 1);

            // go through 7 hits & status checks
            for(int i=0; i < 7*2; i++) {
                station = getQuest().getNextStation(game, 1);
            }

            assertTrue("Player not dead after 7 hits.", station.getText().contains("You have been defeated."));

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
