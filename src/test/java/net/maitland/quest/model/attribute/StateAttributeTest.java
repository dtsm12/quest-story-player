package net.maitland.quest.model.attribute;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by David on 29/12/2016.
 */
public class StateAttributeTest {
    @Test
    public void isValidValueTrue() throws Exception {
        StateAttribute sut = new StateAttribute();
        assertTrue("Should be a valid StateAttribute value", sut.isValidValue("true"));
    }

    @Test
    public void isValidValueFalse() throws Exception {
        StateAttribute sut = new StateAttribute();
        assertTrue("Should be a valid StateAttribute value", sut.isValidValue("false"));
    }

    @Test
    public void isValidValue1() throws Exception {
        StateAttribute sut = new StateAttribute();
        assertFalse("Should not be a valid StateAttribute value", sut.isValidValue("1"));
    }

    @Test
    public void isValidValue0() throws Exception {
        StateAttribute sut = new StateAttribute();
        assertFalse("Should not be a valid StateAttribute value", sut.isValidValue("0"));
    }

    @Test
    public void isValidValueY() throws Exception {
        StateAttribute sut = new StateAttribute();
        assertFalse("Should not be a valid StateAttribute value", sut.isValidValue("Y"));
    }

    @Test
    public void isValidValueN() throws Exception {
        StateAttribute sut = new StateAttribute();
        assertFalse("Should not be a valid StateAttribute value", sut.isValidValue("N"));
    }

}