package net.maitland.quest.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by David on 03/01/2017.
 */
public class GameStation {

    private String id;
    private String text;
    private List<GameChoice> choices;

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

    public List<GameChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<GameChoice> choices) {
        this.choices = Collections.unmodifiableList(choices);
    }
}
