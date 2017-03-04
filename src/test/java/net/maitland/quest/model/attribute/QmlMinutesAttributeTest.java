package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by David on 28/02/2017.
 */
public class QmlMinutesAttributeTest {

    @Test
    public void testLowNumber()
    {
        long start = new Date().getTime();

        Game game = new Game(new About("test", "test"));
        game.put(new StringAttribute(Game.QML_START_TIME, String.valueOf((new Date()).getTime()-7*60*1000)));

        QmlMinutesAttribute sut = new QmlMinutesAttribute();

        String result = sut.replace("[qmlMinutes]", game);

        long end = new Date().getTime();

        long maxDelta = (end-start)/(60*1000);

        int resultInt = Integer.parseInt(result);

        assertTrue("qmlSeconds number incorrect: " + resultInt, resultInt >= 7 && resultInt <= 7+maxDelta);
    }
    @Test
    public void testHighNumber()
    {
        QmlMinutesAttribute sut = new QmlMinutesAttribute();

        long start = new Date().getTime();

        Game game = new Game(new About("test", "test"));
        game.put(new StringAttribute(Game.QML_START_TIME, String.valueOf((new Date()).getTime()-(sut.MAX_MINUTES+1)*60*1000)));

        String result = sut.replace("[qmlMinutes]", game);

        long end = new Date().getTime();

        long maxDelta = (end-start)/1000;

        int resultInt = Integer.parseInt(result);

        assertEquals("qmlMinutes not max number", sut.MAX_MINUTES, resultInt);
    }
}
