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
public class QmlSecondsAttributeTest {

    @Test
    public void testLowNumber()
    {
        long start = new Date().getTime();

        Game game = new Game(new About("test", "test"));
        game.put(new StringAttribute(Game.QML_START_TIME, String.valueOf((new Date()).getTime()-50*1000)));

        QmlSecondsAttribute sut = new QmlSecondsAttribute();

        String result = sut.replace("qmlSeconds", game);

        long end = new Date().getTime();

        long maxDelta = (end-start)/1000;

        int resultInt = Integer.parseInt(result);

        assertTrue("qmlSeconds number incorrect: " + resultInt, resultInt >= 50 && resultInt <= 50+maxDelta);
    }

    @Test
    public void testHighNumber()
    {
        QmlSecondsAttribute sut = new QmlSecondsAttribute();
        Game game = new Game(new About("test", "test"));
        game.put(new StringAttribute(Game.QML_START_TIME, String.valueOf((new Date()).getTime()-((sut.MAX_SECONDS+1)*1000))));

        String result = sut.replace("qmlSeconds", game);
        int resultInt = Integer.parseInt(result);

        assertEquals("qmlSeconds not max number", sut.MAX_SECONDS, resultInt);
    }
}
