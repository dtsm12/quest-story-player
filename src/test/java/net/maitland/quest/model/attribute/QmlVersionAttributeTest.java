package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by David on 28/02/2017.
 */
public class QmlVersionAttributeTest {

    @Test
    public void it_will_replace_with_version()
    {
        QmlVersionAttribute sut = new QmlVersionAttribute();

        String result = sut.replace("[qmlVersion]", new Game(new About("test", "test")));

        assertEquals("qmlVersion didn't resolve to right version", "2", result);
    }
}
