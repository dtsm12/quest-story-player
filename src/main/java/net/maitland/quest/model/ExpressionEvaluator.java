package net.maitland.quest.model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 23/12/2016.
 */
public class ExpressionEvaluator {

    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine expressionEngine = mgr.getEngineByName("JavaScript");

    public boolean check(IfSection ifSection, QuestState attributes) throws QuestStateException {

        String expression = ifSection.getCheck();
        return check(expression, attributes);
    }

    public boolean check(Choice choice, QuestState attributes) throws QuestStateException {

        String expression = choice.getCheck();
        return check(expression == null ? "true" : expression, attributes);
    }

    public boolean check(String expression, QuestState attributes) throws QuestStateException {

        boolean check = false;

        String result = evaluateExpression(expression, attributes);
        check = "true".equalsIgnoreCase(result);

        return check;
    }

    public String evaluateExpression(Attribute attribute, QuestState attributes) throws QuestStateException {
        String expression = attribute.getExpressionValue();

        return evaluateExpression(expression, attributes);
    }

    public String evaluateExpression(String expression, QuestState attributes) throws QuestStateException {
        String result = expression;
        expression = attributes.replace(expression);

        try {
            Object obj = expressionEngine.eval(expression);
            result = obj == null ? "" : obj.toString();
        } catch (ScriptException e) {
            throw new QuestStateException(String.format("Error evaluating expression '%s'", expression), e);
        }
        return result;
    }

    public List<String> extractAttributeNames(String value) {
        Pattern attrPattern = Pattern.compile("(\\[[a-zA-z0-9\\s]+?\\])");
        List<String> attributeNames = new ArrayList<>();

        if (value != null) {
            Matcher matcher = attrPattern.matcher(value);

            while (matcher.find()) {
                String name = matcher.group();
                attributeNames.add(name);
            }
        }
        return attributeNames;
    }
}
