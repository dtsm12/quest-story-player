package net.maitland.quest.model;

import java.util.*;

/**
 * Created by David on 19/12/2016.
 */
public class QuestState {

    public static final String MATH_FLOOR_MATH_RANDOM_6_1_TO_STRING = "(Math.floor(Math.random() * 6) + 1).toString()";
    public static final String MATH_FLOOR_MATH_RANDOM_100_1_TO_STRING = "(Math.floor(Math.random() * 100) + 1).toString()";
    public static final String MATH_FLOOR_MATH_RANDOM_1000_1_TO_STRING = "(Math.floor(Math.random() * 1000) + 1).toString()";
    public static final String MATH_FLOOR_MATH_RANDOM_13_1_TO_STRING = "(Math.floor(Math.random() * 13) + 1).toString()";

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    private Map<String, Attribute> attributes = new HashMap<>();

    public QuestState() {
        this(null);

        // add keyword attributes
        this.put(new OperatorAttribute(" greater ", " > "));
        this.put(new OperatorAttribute(" lower ", " < "));
        this.put(new OperatorAttribute(" and ", " && "));
        this.put(new OperatorAttribute(" or ", " || "));
        this.put(new OperatorAttribute("not ", "! "));
        this.put(new OperatorAttribute(" = ", " == "));
        this.put(new TemplateAttribute("{random (\\d+), (\\d+)}", "(Math.floor(Math.random() * %2$s) + %1$s).toString()"));
    }

    public QuestState(Map<String, Attribute> attributes) {

        if (attributes != null) {
            this.attributes = new HashMap<>(attributes);
        } else {
            this.attributes = new HashMap<>();
        }
    }

    public void put(Attribute attribute){
        this.attributes.put(attribute.getName(), attribute);
    }

    public QuestState copyAttributes() {
        return new QuestState(this.attributes);
    }

    public Map<String, Attribute> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    public String getAttributeValue(String name) {
        return this.attributes.get(name).getValue();
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public String replace(String value) {

        for (Attribute a : this.attributes.values())
        {
            value = a.replace(value);
        }

        return value;
    }

    public boolean check(Choice choice) throws QuestStateException {

        return expressionEvaluator.check(choice, this.copyAttributes());
    }

    public boolean check(IfSection ifSection) throws QuestStateException {

        return expressionEvaluator.check(ifSection, this.copyAttributes());
    }

    public String toStateText(String text) throws QuestStateException {

        List<String> attributeNames = this.expressionEvaluator.extractAttributeNames(text);

        for (String a : attributeNames) {
            text = text.replace(a, this.getAttributeValue(a));
        }

        return text;
    }
}
