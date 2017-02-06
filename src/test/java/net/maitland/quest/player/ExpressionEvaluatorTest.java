package net.maitland.quest.player;

import net.maitland.quest.model.ExpressionEvaluator;
import net.maitland.quest.model.NumberAttribute;
import net.maitland.quest.model.QuestState;
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

        String result = sut.evaluateExpression(QuestState.MATH_FLOOR_MATH_RANDOM_6_1_TO_STRING, new HashMap<String, String>());

        assertTrue(String.format("Random number '%s' is not accepted as NumberAttribute", result), number.isValidValue(result));

    }

    @Test
    public void evaluateNegativeStringEqualityExpression() throws Exception {

        ExpressionEvaluator sut = new ExpressionEvaluator();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("[cardNumber]", "2");
        attributes.put("=", "==");
        attributes.put("not", "!");
        attributes.put("and", "&&");
        attributes.put("[came from]", "'south'");

        String result = sut.evaluateExpression("[cardNumber] = 2 and [came from] != 'north'", attributes);

        assertTrue("Expression not true", Boolean.parseBoolean(result));

    }

}