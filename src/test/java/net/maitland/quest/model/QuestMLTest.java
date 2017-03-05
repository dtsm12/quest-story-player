package net.maitland.quest.model;

import net.maitland.quest.model.attribute.StringAttribute;
import net.maitland.quest.parser.sax.SaxQuestParser;
import net.maitland.quest.player.ConsolePlayer;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static junit.framework.TestCase.*;

/**
 * Created by David on 03/02/2017.
 */
public class QuestMLTest {


    @Test
    public void test_all_attribute_types() {

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            // enter quest
            GameStation station = sut.getNextStation(game, 0);
            List<GameChoice> choices = station.getChoices();

            while ("end".equals(station.getId()) == false){

                assertEquals(station.getId() + " does not have 2 choices.", 2, choices.size());
                station = sut.getNextStation(game, 2);
                choices = station.getChoices();
            }


        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    protected Quest getQuest() throws Exception {
        Quest q = null;
        InputStream is = null;

        try {
            is = ConsolePlayer.class.getClassLoader().getResourceAsStream("questml-test.xml");
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
