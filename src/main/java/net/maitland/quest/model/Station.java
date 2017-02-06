package net.maitland.quest.model;

import java.util.List;

/**
 * Created by David on 19/12/2016.
 */
public interface Station {

    public String getId();

    public Text visit() throws QuestStateException;

    public List<Choice> getChoices() throws QuestStateException;

    public Choice getChoice(String choiceId) throws QuestStateException;
}
