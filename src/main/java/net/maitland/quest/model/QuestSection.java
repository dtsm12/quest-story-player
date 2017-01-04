package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import net.maitland.quest.player.QuestStateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 18/12/2016.
 */
public class QuestSection {

    private Text text;

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

    private List<Attribute> attributes = new ArrayList<>();

    @JacksonXmlProperty(localName = "if")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<IfSection> conditions = new ArrayList<>();

    @JacksonXmlProperty(localName = "else")
    private ElseSection elseCondition;

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public List<Choice> getChoices() throws QuestStateException {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public List<NumberAttribute> getNumberAttributes() {
        return numberAttributes;
    }

    public void setNumberAttributes(List<NumberAttribute> numberAttributes) {
        this.numberAttributes = numberAttributes;
        addAttributes(numberAttributes);
    }

    public List<StringAttribute> getStringAttributes() {
        return stringAttributes;
    }

    public void setStringAttributes(List<StringAttribute> stringAttributes) {
        this.stringAttributes = stringAttributes;
        addAttributes(stringAttributes);
    }

    public List<StateAttribute> getStateAttributes() {
        return stateAttributes;
    }

    public void setStateAttributes(List<StateAttribute> stateAttributes) {
        this.stateAttributes = stateAttributes;
        addAttributes(stateAttributes);
    }

    private void addAttributes(List<? extends Attribute> attributes) {

        for(Attribute a : attributes)
        {
            a.setName(String.format("[%s]", a.getName()));
        }
        this.attributes.addAll(attributes);
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<IfSection> getConditions() {
        return conditions;
    }

    public void setConditions(List<IfSection> conditions) {
        this.conditions = conditions;
    }

    public ElseSection getElseCondition() {
        return elseCondition;
    }

    public void setElseCondition(ElseSection elseCondition) {
        this.elseCondition = elseCondition;
    }
}
