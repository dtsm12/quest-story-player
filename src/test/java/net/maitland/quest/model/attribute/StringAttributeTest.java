package net.maitland.quest.model.attribute;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 04/03/2017.
 */
public class StringAttributeTest {

    @Test
    public void updateState() {

        StringAttribute sut = new StringAttribute("test", "value1");
        sut.setValue("value2");

        assertEquals("New value after update state is incorrect", "value2", sut.getValue());
    }
}
