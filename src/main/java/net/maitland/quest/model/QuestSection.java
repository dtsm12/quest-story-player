package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 18/12/2016.
 */
public class QuestSection {

    private Text text = null;

    @JacksonXmlProperty(localName = "choice")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Choice> choices = new ArrayList<>();

    @JacksonXmlProperty(localName = "number")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<NumberAttribute> numberAttributes = new ArrayList<>();

    @JacksonXmlProperty(localName = "string")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<StringAttribute> stringAttributes = new ArrayList<>();

    @JacksonXmlProperty(localName = "state")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<StateAttribute> stateAttributes = new ArrayList<>();

    private List<Attribute> preVisitAttributes = new ArrayList<>();
    private List<Attribute> postVisitAttributes = new ArrayList<>();

    @JacksonXmlProperty(localName = "if")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<IfSection> conditions = new ArrayList<>();

    @JacksonXmlProperty(localName = "else")
    private ElseSection elseCondition;

    protected boolean isPreVisit() {
        return this.text == null && this.conditions.size() == 0;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public List<Choice> getChoices() {
        return Collections.unmodifiableList(choices);
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addChoice(Choice choice) {
        this.choices.add(choice);
    }

    public List<NumberAttribute> getNumberAttributes() {
        return Collections.unmodifiableList(numberAttributes);
    }

    public void setNumberAttributes(List<NumberAttribute> numberAttributes) {
        this.numberAttributes = numberAttributes;
        addAttributes(numberAttributes);
    }

    public List<StringAttribute> getStringAttributes() {
        return Collections.unmodifiableList(stringAttributes);
    }

    public void setStringAttributes(List<StringAttribute> stringAttributes) {
        this.stringAttributes = stringAttributes;
        addAttributes(stringAttributes);
    }

    public List<StateAttribute> getStateAttributes() {
        return Collections.unmodifiableList(stateAttributes);
    }

    public void setStateAttributes(List<StateAttribute> stateAttributes) {
        this.stateAttributes = stateAttributes;
        addAttributes(stateAttributes);
    }

    public <A extends Attribute> void addAttribute(A attribute) {

        attribute.setName(String.format("[%s]", attribute.getName()));
        if (this.isPreVisit()) {
            this.preVisitAttributes.add(attribute);
        } else {
            this.postVisitAttributes.add(attribute);
        }

        if (attribute instanceof NumberAttribute && this.numberAttributes.contains(attribute) == false) {
            this.numberAttributes.add((NumberAttribute) attribute);
        }

        if (attribute instanceof StringAttribute && this.stringAttributes.contains(attribute) == false) {
            this.stringAttributes.add((StringAttribute) attribute);
        }

        if (attribute instanceof StateAttribute && this.stateAttributes.contains(attribute) == false) {
            this.stateAttributes.add((StateAttribute) attribute);
        }
    }

    private void addAttributes(List<? extends Attribute> attributes) {

        for (Attribute a : attributes) {
            addAttribute(a);
        }
    }

    public List<Attribute> getPreVisitAttributes() {
        return Collections.unmodifiableList(this.preVisitAttributes);
    }

    public List<Attribute> getPostVisitAttributes() {
        return Collections.unmodifiableList(this.postVisitAttributes);
    }

    public List<IfSection> getConditions() {
        return Collections.unmodifiableList(conditions);
    }

    public void setConditions(List<IfSection> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(IfSection ifSection) {
        this.conditions.add(ifSection);
    }

    public ElseSection getElseCondition() {
        return elseCondition;
    }

    public void setElseCondition(ElseSection elseCondition) {
        this.elseCondition = elseCondition;
    }
}
