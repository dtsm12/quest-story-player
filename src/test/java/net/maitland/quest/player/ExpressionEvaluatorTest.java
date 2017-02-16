package net.maitland.quest.player;

import net.maitland.quest.model.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by David on 28/12/2016.
 */
public class ExpressionEvaluatorTest {

    @Test
    public void evaluateRandomNumberExpression() throws Exception {

        ExpressionEvaluator sut = new ExpressionEvaluator();
        NumberAttribute number = new NumberAttribute();

        String result = sut.evaluateExpression(QuestState.MATH_FLOOR_MATH_RANDOM_6_1_TO_STRING, new QuestState());

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