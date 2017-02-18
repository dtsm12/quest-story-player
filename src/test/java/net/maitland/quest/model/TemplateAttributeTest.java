package net.maitland.quest.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by David on 18/02/2017.
 */
public class TemplateAttributeTest {

    @Test
    public void replace() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{random (\\d), (\\d)}", "(Math.floor(Math.random() * %2$s) + %1$s).toString()");

        String replaced = sut.replace("{random 1, 6}");

        assertEquals("Replaced value is incorrect", "(Math.floor(Math.random() * 6) + 1).toString()", replaced);

    }

}