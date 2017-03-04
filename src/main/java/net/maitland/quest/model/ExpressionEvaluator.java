package net.maitland.quest.model;

import net.maitland.quest.model.attribute.Attribute;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 23/12/2016.
 */
public class ExpressionEvaluator {

    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine expressionEngine;

    protected ScriptEngine getExpressionEngine() throws ScriptException {
        if (this.expressionEngine == null) {
            this.expressionEngine = mgr.getEngineByName("JavaScript");

            for (String fn : getGlobalFunctions())
            {
                this.expressionEngine.eval(fn);
            }
        }

        return this.expressionEngine;
    }

    protected Collection<String> getGlobalFunctions()
    {
        List<String> functions = new ArrayList<>();
        functions.add(getOrdinalFunction());
        functions.add(getRepeatFunction());

        return functions;
    }

    protected String getRepeatFunction() {

        return "String.prototype.repeat = function(n) {\n" +
                "    var s = this.toString(); \n" +
                "    var r = '';\n" +
                "    for(var i=0; i < n;i++)\n" +
                "      r += s;\n" +
                "    return r;\n" +
                "}";
    }

    protected String getOrdinalFunction()
    {
        return "String.prototype.getOrdinal = function() {\n" +
                "    var n = this.toString(); \n" +
                "    var s=[\"th\",\"st\",\"nd\",\"rd\"],\n" +
                "    v=n%100;\n" +
                "    return n+(s[(v-20)%10]||s[v]||s[0]);\n" +
                "}";
    }

    public boolean check(IfSection ifSection, Game game) throws QuestStateException {

        String expression = ifSection.getCheck();
        return check(expression, game, true);
    }

    public boolean check(Choice choice, Game game) throws QuestStateException {

        String expression = choice.getCheck();
        return check(expression == null ? "true" : expression, game, true);
    }

    public boolean check(String expression, Game game, boolean isCheck) throws QuestStateException {

        boolean check = false;

        String result = evaluateExpression(expression, game, isCheck);
        check = "true".equalsIgnoreCase(result);

        return check;
    }

    public String evaluateExpression(Attribute attribute, Game game, boolean isCheck) throws QuestStateException {
        String expression = attribute.getExpressionValue();

        return evaluateExpression(expression, game, isCheck);
    }

    public String evaluateExpression(String expression, Game game) throws QuestStateException {
        return evaluateExpression(expression, game, true);
    }

    public String evaluateExpression(String expression, Game game, boolean isCheck) throws QuestStateException {
        String result = expression;
        expression = game.replace(expression, isCheck);
        result = evaluateExpression(expression);
        return result;
    }

    public String evaluateExpression(String expression) {
        String result;
        try {
            Object obj = getExpressionEngine().eval(expression);
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
