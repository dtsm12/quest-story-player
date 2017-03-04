package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by David on 04/03/2017.
 */
public class OperatorAttributeTest {
    @Test
    public void replaceForCheck() throws Exception {
        OperatorAttribute sut = new OperatorAttribute(" and ", " && ");
        String ret = sut.replace("true and false", new Game(new About("test", "test")), true);
        assertEquals("OperatorAttribute did not replace value for check", "true && false", ret);
    }

    @Test
    public void replaceForText() throws Exception {
        OperatorAttribute sut = new OperatorAttribute(" and ", " && ");
        String ret = sut.replace("true and false", new Game(new About("test", "test")), false);
        assertEquals("OperatorAttribute did not replace value for check", "true and false", ret);
    }
}