package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import net.maitland.quest.model.Quest;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 02/03/2017.
 */
public class QmlStationAttributeTest {

    @Test
    public void multipleStations() {

        Game game = new Game(new About("test", "test"));
        game.getQuestPath().push("one");
        game.getQuestPath().push("two");
        game.getQuestPath().push("three");

        QmlStationAttribute sut = new QmlStationAttribute();

        String result = sut.replace("[qmlStation]", game, false);

        assertEquals("QmlStationAttribute returned wrong value", "three", result);
    }

    @Test
    public void stationInText() {

        QmlStationAttribute sut = new QmlStationAttribute();

        Game game = new Game(new About("test", "test"), Collections.singletonMap(sut.getName(), sut));
        game.getQuestPath().push("one");
        game.getQuestPath().push("two");
        game.getQuestPath().push("three");

        String result = game.toStateText("You are at stage [qmlStation]");

        assertEquals("QmlStationAttribute returned wrong value for text", "You are at stage three", result);
    }
}
