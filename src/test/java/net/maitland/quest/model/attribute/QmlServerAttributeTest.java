package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 28/02/2017.
 */
public class QmlServerAttributeTest {

    @Test
    public void it_will_replace_with_true()
    {
        QmlServerAttribute sut = new QmlServerAttribute();

        String result = sut.replace("[qmlServer]", new Game(new About("test", "test")));

        assertEquals("qmlServer didn't resolve to true", "true", result);
    }
}
