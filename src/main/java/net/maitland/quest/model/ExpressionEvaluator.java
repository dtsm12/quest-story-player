package net.maitland.quest.model;

import net.maitland.quest.model.attribute.Attribute;
import net.maitland.quest.model.attribute.StateAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by David on 23/12/2016.
 */
public class ExpressionEvaluator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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

    public String toStateText(List<Text> texts, Game game, Map<String, Attribute> attributes) throws QuestStateException {

        StringBuilder sb = new StringBuilder();

        for (Text t : texts) {
            if (check(t, game, attributes)) {
                sb.append(toStateText(t.getValue(), game, attributes));
            }
        }

        return sb.toString();
    }

    public String toStateText(String text, Game game, Map<String, Attribute> attributes) throws QuestStateException {
        return this.replace(text, false, game, attributes);
    }

    public boolean check(Conditional conditional, Game game, Map<String, Attribute> attributes) throws QuestStateException {

        String expression = conditional.getCheck();
        return check(expression == null ? "true" : expression, game, attributes, true);
    }

    public boolean check(String expression, Game game, Map<String, Attribute> attributes, boolean isCheck) throws QuestStateException {

        boolean check = false;

        String result = evaluateExpression(expression, game, attributes, isCheck);
        check = "true".equalsIgnoreCase(result);

        return check;
    }

    public String evaluateExpression(Attribute attribute, Game game, Map<String, Attribute> attributes, boolean isCheck) throws QuestStateException {
        String expression = attribute.getValue(true);

        return evaluateExpression(expression, game, attributes, isCheck);
    }

    public String evaluateExpression(String expression, Game game, Map<String, Attribute> attributes) throws QuestStateException {
        return evaluateExpression(expression, game, attributes, true);
    }

    public String evaluateExpression(String expression, Game game, Map<String, Attribute> attributes, boolean isCheck) throws QuestStateException {
        String result = expression;
        expression = this.replace(expression, isCheck, game, attributes);
        result = evaluateExpression(expression);
        return result;
    }

    protected String replace(String value, boolean isCheck, Game game, Map<String, Attribute> attributes) {

        this.log.trace("Replacing '{}'", value);

        for (Attribute a : attributes.values()) {
            value = a.replace(value, game, isCheck);
        }

        this.log.trace("Replaced '{}'", value);

        return value;
    }

    public List<GameChoice> getQuestStateChoices(List<Choice> choices, Game game, Map<String, Attribute> attributes) throws QuestStateException {

        List<GameChoice> gameChoices = choices.stream().filter(choice -> check(choice, game, attributes)).map(choice -> newQuestStateChoice(choice, game, attributes)).collect(Collectors.toList());
        return gameChoices;
    }

    protected GameChoice newQuestStateChoice(Choice c, Game game, Map<String, Attribute> attributes) throws QuestStateException {
        GameChoice stateChoice = new GameChoice();
        stateChoice.setStationId(this.toStateText(c.getStationId(), game, attributes));
        stateChoice.setText(this.toStateText(c.getText(),game,  attributes));
        return stateChoice;
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

    public void updateState(Game game, Map<String, Attribute> attributes, Collection<Attribute> newAttributes) throws QuestStateException {

        for (Attribute a : newAttributes) {
            //evaluate the attributes value - only treat as check for StateAttribute
            String attrValue = evaluateExpression(a, game, attributes, a instanceof StateAttribute);

            // check it's the right type of value
            if (a.isValidValue(attrValue) == false) {
                throw new QuestStateException(String.format("Attribute '%s' expression '%s' evaluates to incorrect type", a.getName(), attrValue));
            }

            log.debug("Setting attribute '{}' to value '{}'", a.getName(), attrValue);

            // update the Quest's state
            attributes.put(a.getName(), a.updateValue(attrValue));
        }
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
