package net.maitland.quest.model;

import net.maitland.quest.player.ExpressionEvaluator;
import net.maitland.quest.player.QuestStateException;

import java.util.List;
import java.util.Map;

/**
 * Created by David on 23/01/2017.
 */
public class GameInstance {

    private QuestState questState = new QuestState();
    private QuestState previousQuestState = new QuestState();
    private boolean isInitialised;

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    /* Use default/package access modifier - should be obtained from QuestInstance */
    GameInstance() {
    }

    public QuestState getCurrentState()
    {
        return this.questState;
    }

    public QuestState getPreviousState()
    {
        return this.previousQuestState;
    }

    public void updateState(List<Attribute> attributes) throws QuestStateException
    {
        Map<String, String> newState = this.questState.copyAttributes();

        for (Attribute a : attributes) {
            //evaluate the attributes value
            String attrValue = expressionEvaluator.evaluateExpression(a, newState);

            // check it's the right type of value
            if (a.isValidValue(attrValue) == false) {
                throw new QuestStateException(String.format("Attribute '%s' expression '%s' evaluates to incorrect type", a.getName(), a.getValue()));
            }

            // update the Quest's state
            newState.put(a.getName(), attrValue);
        }

        this.previousQuestState = this.questState;
        this.questState = new QuestState(newState);
    }
}
