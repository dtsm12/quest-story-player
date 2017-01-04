package net.maitland.quest.player;

import net.maitland.quest.model.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

/**
 * Created by David on 19/12/2016.
 */
public class QuestInstance {

    private QuestState questState;
    private Quest quest;
    private Deque<QuestStation> questPath = new ArrayDeque<>();
    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    public QuestInstance(Quest quest) {
        this.quest = quest;
    }

    public QuestStateStation getNextStation(String choiceId) throws QuestStateException, ChoiceNotPossibleException {

        QuestStation questStation = null;
        QuestState currentQuestState = getQuestState();

        if (choiceId == null) {
            choiceId = "start";
        }

        if (this.questPath.isEmpty()) {
            for (QuestStation os : quest.getStations()) {
                if (choiceId.equals(os.getId())) {
                    questStation = os;
                    break;
                }
            }
        } else {
            // Get choice
            QuestStation currentStation = this.questPath.peek();
            Choice choice = currentStation.getChoice(currentQuestState, choiceId);

            // check it was possible
            if (choice == null) {
                throw new ChoiceNotPossibleException(String.format("The choice %s is not possible.", choiceId));
            }

            questStation = choice.getStation();
        }


        if (questStation == null) {
            throw new QuestStateException(String.format("Station %s is not found.", choiceId));
        }

        // resolve back references
        QuestStation nextStation;
        if(questStation.getId().equals(QuestStation.BACK_STATION_ID))
        {
            this.questPath.pop();
            nextStation = this.questPath.peek();
        }
        else
        {
            nextStation = questStation;
        }

        // prepare next station data before updating state (visiting station)
        QuestStateStation retStation = new QuestStateStation();
        retStation.setId(nextStation.getId());
        retStation.setText(nextStation.getText(currentQuestState).getValue());
        retStation.setChoices(nextStation.getChoices(currentQuestState));

        // update quest state
        this.questState = nextStation.visit(currentQuestState);

        // add to quest path
        this.questPath.push(nextStation);

        // return relevant data for choice
        return retStation;
    }

    protected QuestState getQuestState() {

        if(this.questState == null)
        {
            this.questState = new QuestState(this.quest.collectAllAttributes());
        }

        return this.questState;
    }

    public Quest getQuest() {
        return quest;
    }
}
