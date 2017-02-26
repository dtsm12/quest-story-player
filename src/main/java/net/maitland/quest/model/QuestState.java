package net.maitland.quest.model;

import net.maitland.quest.model.attribute.Attribute;
import net.maitland.quest.model.attribute.OperatorAttribute;
import net.maitland.quest.model.attribute.StatesFunctionAttribute;
import net.maitland.quest.model.attribute.TemplateAttribute;

import java.util.*;

/**
 * Created by David on 19/12/2016.
 */
public class QuestState {

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    private Map<String, Attribute> attributes = new HashMap<>();

    public QuestState() {
        this(null);

        // add built-in attributes
        this.put(new OperatorAttribute(" greater ", " > "));
        this.put(new OperatorAttribute(" lower ", " < "));
        this.put(new OperatorAttribute(" and ", " && "));
        this.put(new OperatorAttribute(" or ", " || "));
        this.put(new OperatorAttribute("not ", "! "));
        this.put(new OperatorAttribute(" = ", " == "));
        this.put(new TemplateAttribute("{random (\\d+), (\\d+)}", "(Math.floor(Math.random() * %2$s) + %1$s).toString()"));
        this.put(new StatesFunctionAttribute());
        this.put(new TemplateAttribute("{contains ([^{}]+), ([^{}]+)}", "(%1$s.indexOf(%2$s) > -1)"));
        this.put(new TemplateAttribute("{containsWord ([^{}]+), '([\\s\\w]+)'}", "(%1$s.search(/\\b%2$s\\b/) > -1)"));
        this.put(new TemplateAttribute("{verbose (\\d+)}", "'%1$s'.getOrdinal()"));
        this.put(new TemplateAttribute("{upper ([^{}]+)}", "%1$s.toUpperCase()"));
        this.put(new TemplateAttribute("{lower ([^{}]+)}", "%1$s.toLowerCase()"));
        this.put(new TemplateAttribute("{repeatString ([^{}]+), (\\d)}", "%1$s.repeat(%2$s)"));
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
            value = a.replace(value, this);
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
