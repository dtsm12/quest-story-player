package net.maitland.quest.model;

import java.util.List;

/**
 * Created by David on 03/01/2017.
 */
public class QuestStateStation {

    private String id;
    private String text;
    private List<Choice> choices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
