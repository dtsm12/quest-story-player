package net.maitland.quest.model;

import net.maitland.quest.model.attribute.NumberAttribute;
import net.maitland.quest.model.attribute.OperatorAttribute;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.Assert.*;

/**
 * Created by David on 28/12/2016.
 */
public class ExpressionEvaluatorTest {

    @Test
    public void prototypeFunction() throws Exception {

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine expressionEngine = mgr.getEngineByName("JavaScript");

        expressionEngine.eval("String.prototype.addDot = function() {return this.toString()+'.';}");

        assertEquals("Prototype function returned incorrect value", "X.", expressionEngine.eval("'X'.addDot();"));
    }

    @Test
    public void globalFunction() throws Exception {

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine expressionEngine = mgr.getEngineByName("JavaScript");

        expressionEngine.eval("function addOne(arg) {return arg+1;}");

        assertEquals("Global function returned incorrect value", 2L, expressionEngine.eval("addOne(1)"));
    }

    @Test
    public void evaluateRandomNumberExpression() throws Exception {

        ExpressionEvaluator sut = new ExpressionEvaluator();
        NumberAttribute number = new NumberAttribute();

        String result = sut.evaluateExpression("(Math.floor(Math.random() * 6) + 1).toString()", new QuestState());

        assertTrue(String.format("Random number '%s' is not accepted as NumberAttribute", result), number.isValidValue(result));

    }

    @Test
    public void evaluateExpression() throws Exception {

        ExpressionEvaluator sut = new ExpressionEvaluator();

        String result = sut.evaluateExpression("'0' > '6' && '4' > '0'", new QuestState());

        assertEquals("Result not evaluated", "false", result);

    }

    @Test
    public void evaluateNegativeStringEqualityExpression() throws Exception {

        ExpressionEvaluator sut = new ExpressionEvaluator();
        QuestState attributes = new QuestState();
        attributes.put(new NumberAttribute("[cardNumber]", "2"));
        attributes.put(new OperatorAttribute(" = ", " == "));
        attributes.put(new OperatorAttribute(" not ", " ! "));
        attributes.put(new OperatorAttribute(" and ", " && "));
        attributes.put(new OperatorAttribute("[came from]", "'south'"));

        String result = sut.evaluateExpression("[cardNumber] = 2 and [came from] != 'north'", attributes);

        assertTrue("Expression not true", Boolean.parseBoolean(result));

    }

}