package net.maitland.quest.model.attribute;

import net.maitland.quest.model.ExpressionEvaluator;
import net.maitland.quest.model.attribute.NumberAttribute;
import net.maitland.quest.model.attribute.TemplateAttribute;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by David on 18/02/2017.
 */
public class TemplateAttributeTest {

    @Test
    public void random() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{random (\\d+), (\\d+)}", "(Math.floor(Math.random() * %2$s) + %1$s).toString()");

        String replaced = sut.replace("{random 1, 6}", null);

        assertEquals("Replaced value is incorrect", "(Math.floor(Math.random() * 6) + 1).toString()", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();
        NumberAttribute number = new NumberAttribute();

        String result = ee.evaluateExpression(replaced);

        assertTrue("random function results is not a number", number.isValidValue(result));

    }

    public void verbose(String number, String expected) throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{verbose (\\d+)}", "'%1$s'.getOrdinal()");

        String replaced = sut.replace("{verbose " + number + "}", null);

        assertEquals("Verbose value is incorrect", "'" + number + "'.getOrdinal()", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("verbose function results is incorrect", expected, result);

    }

    @Test
    public void contains() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{contains ([^{}]+), ([^{}]+)}", "(%1$s.indexOf(%2$s) > -1)");

        String replaced = sut.replace("{contains 'this is a test', 'test'}", null);

        assertEquals("Replaced value is incorrect", "('this is a test'.indexOf('test') > -1)", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("contains function returned wrong result", "true", result);

    }

    @Test
    public void containsNonAlpha() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{contains ([^{}]+), ([^{}]+)}", "(%1$s.indexOf(%2$s) > -1)");

        String replaced = sut.replace("{contains 'this is @ test', 'test'}", null);

        assertEquals("Replaced value is incorrect", "('this is @ test'.indexOf('test') > -1)", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("contains function returned wrong result", "true", result);

    }

    @Test
    public void lower() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{lower ([^{}]+)}", "%1$s.toLowerCase()");

        String replaced = sut.replace("{lower 'this IS a tEst'}", null);

        assertEquals("Replaced value is incorrect", "'this IS a tEst'.toLowerCase()", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("lower function returned wrong result", "this is a test", result);

    }

    @Test
    public void upper() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{upper ([^{}]+)}", "%1$s.toUpperCase()");

        String replaced = sut.replace("{upper 'this IS a tEst'}", null);

        assertEquals("Replaced value is incorrect", "'this IS a tEst'.toUpperCase()", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("upper function returned wrong result", "THIS IS A TEST", result);

    }

    @Test
    public void repeat() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{repeatString ([^{}]+), (\\d)}", "%1$s.repeat(%2$s)");

        String replaced = sut.replace("{repeatString 'rep this ', 3}", null);

        assertEquals("Replaced value is incorrect", "'rep this '.repeat(3)", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("repeat function returned wrong result", "rep this rep this rep this ", result);

    }

    @Test
    public void repeatAndUpper() throws Exception {

        TemplateAttribute repeat = new TemplateAttribute("{repeatString ([^{}]+), (\\d)}", "%1$s.repeat(%2$s)");

        TemplateAttribute upper = new TemplateAttribute("{upper ([^{}]+)}", "%1$s.toUpperCase()");

        String test = "{upper {repeatString 'rep this ', 3}}";
        test = repeat.replace(test, null);
        test = upper.replace(test, null);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(test);

        assertEquals("repeat and upper functions returned wrong result", "REP THIS REP THIS REP THIS ", result);

    }

    @Test
    public void containsWord() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{containsWord ([^{}]+), '([\\w]+)'}", "(%1$s.search(/\\b%2$s\\b/) > -1)");

        String replaced = sut.replace("{containsWord 'this is a test', 'test'}", null);

        assertEquals("Replaced value is incorrect", "('this is a test'.search(/\\btest\\b/) > -1)", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("containsWord function returned wrong result", "true", result);

    }

    @Test
    public void containsWordNegative() throws Exception {

        TemplateAttribute sut = new TemplateAttribute("{containsWord ('[\\s\\w]+'), '([\\s\\w]+)'}", "(%1$s.search(/\\b%2$s\\b/) > -1)");

        String replaced = sut.replace("{containsWord 'this is a test', 'tes'}", null);

        assertEquals("Replaced value is incorrect", "('this is a test'.search(/\\btes\\b/) > -1)", replaced);

        ExpressionEvaluator ee = new ExpressionEvaluator();

        String result = ee.evaluateExpression(replaced);

        assertEquals("containsWord function found word where it wasn't present", "false", result);

    }


    @Test
    public void verboseOne() throws Exception {
        verbose("1", "1st");
    }

    @Test
    public void verboseTwo() throws Exception {
        verbose("22", "22nd");
    }

    @Test
    public void verboseThree() throws Exception {
        verbose("203", "203rd");
    }

    @Test
    public void verboseThirteen() throws Exception {
        verbose("213", "213th");
    }

    @Test
    public void verboseFour() throws Exception {
        verbose("9004", "9004th");
    }

    @Test
    public void verboseFive() throws Exception {
        verbose("87645", "87645th");
    }

    @Test
    public void verboseSix() throws Exception {
        verbose("748256", "748256th");
    }

    @Test
    public void verboseSeven() throws Exception {
        verbose("244957", "244957th");
    }

    @Test
    public void verboseEight() throws Exception {
        verbose("88888888", "88888888th");
    }

    @Test
    public void verboseNine() throws Exception {
        verbose("086464089", "086464089th");
    }

    @Test
    public void verboseTen() throws Exception {
        verbose("10", "10th");
    }
}