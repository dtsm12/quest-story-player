package net.maitland.quest.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by David on 18/02/2017.
 */
public class TemplateAttributeTest {

    @Test
    public void random() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{random (\\d+), (\\d+)}", "(Math.floor(Math.random() * %2$s) + %1$s).toString()");

        String replaced = sut.replace("{random 1, 6}");

        assertEquals("Replaced value is incorrect", "(Math.floor(Math.random() * 6) + 1).toString()", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();
        NumberAttribute number = new NumberAttribute();

        String result = ee.evaluateExpression(replaced);

        assertTrue("random function results is not a number", number.isValidValue(result));

    }

    @Test
    public void contains() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{contains ('[\\s\\w]+'), ('[\\s\\w]+')}", "(%1$s.indexOf(%2$s) > -1)");

        String replaced = sut.replace("{contains 'this is a test', 'test'}");

        assertEquals("Replaced value is incorrect", "('this is a test'.indexOf('test') > -1)", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("contains function returned wrong result", "true", result);

    }

}