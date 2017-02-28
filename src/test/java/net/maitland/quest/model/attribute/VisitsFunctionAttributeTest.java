package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 28/02/2017.
 */
public class VisitsFunctionAttributeTest {

    @Test
    public void singleStationName() {

        VisitsFunctionAttribute sut = new VisitsFunctionAttribute();
        Game game = new Game(new About("test", "test"));
        game.getQuestPath().push("test1");
        game.getQuestPath().push("test2");
        game.getQuestPath().push("test1");
        game.getQuestPath().push("test2");
        game.getQuestPath().push("test1");

        String result = sut.replace("visits(test1)", game);

        assertEquals("Visits attribute gave incorrect result", "3", result);

    }
    @Test
    public void allStations() {

        VisitsFunctionAttribute sut = new VisitsFunctionAttribute();
        Game game = new Game(new About("test", "test"));
        game.getQuestPath().push("test1");
        game.getQuestPath().push("test2");
        game.getQuestPath().push("test1");
        game.getQuestPath().push("test2");
        game.getQuestPath().push("test1");

        String result = sut.replace("visits(*)", game);

        assertEquals("Visits attribute gave incorrect result", "5", result);

    }
}
