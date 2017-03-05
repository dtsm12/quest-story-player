package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 02/03/2017.
 */
public class QmlLastStationAttributeTest {

    @Test
    public void multipleStations() {

        Game game = new Game(new About("test", "test"));
        game.getQuestPath().push("one");
        game.getQuestPath().push("two");
        game.getQuestPath().push("three");

        QmlLastStationAttribute sut = new QmlLastStationAttribute();

        String result = sut.replace("[qmlLastStation]", game, false);

        assertEquals("QmlStationAttribute returned wrong value", "two", result);
    }

    @Test
    public void stationInText() {

        QmlLastStationAttribute sut = new QmlLastStationAttribute();

        Game game = new Game(new About("test", "test"), Collections.singletonMap(sut.getName(), sut));
        game.getQuestPath().push("one");
        game.getQuestPath().push("two");
        game.getQuestPath().push("three");

        String result = game.toStateText("You are at stage [qmlLastStation]");

        assertEquals("QmlStationAttribute returned wrong value for text", "You are at stage two", result);
    }
}
