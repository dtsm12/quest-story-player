package net.maitland.quest.model.attribute;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by David on 22/03/2017.
 */
public class NumberAttributeTest {

    @Test
    public void setMin() throws Exception {
        NumberAttribute sut = new NumberAttribute();
        sut.setName("test");
        sut.setMin("10");
        Attribute newAtt = sut.updateValue("1");
        assertEquals("Minimum value was not honored", "10", newAtt.getValue());
    }

    @Test
    public void setMax() throws Exception {
        NumberAttribute sut = new NumberAttribute();
        sut.setName("test");
        sut.setMax("10");
        Attribute newAtt = sut.updateValue("101");
        assertEquals("Minimum value was not honored", "10", newAtt.getValue());
    }

}