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
    private QuestState previousQuestState;
    private Quest quest;
    private Deque<QuestStation> questPath = new ArrayDeque<>();
    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    public QuestInstance(Quest quest) {
        this.quest = quest;
    }

    public QuestStateStation getNextStation(String choiceId) throws QuestStateException, ChoiceNotPossibleException {

        QuestStation questStation = null;

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
            Choice choice = currentStation.getChoice(this.previousQuestState, choiceId);

            // check it was possible
            if (choice == null) {
                throw new ChoiceNotPossibleException(String.format("The choice %s is not possible.", choiceId));
            }

            questStation = choice.getStation();
        }


        if (questStation == null) {
            throw new QuestStateException(String.format("Station %s is not found.", choiceId));
        }

        return processNextStation(questStation);
    }

    public QuestStateStation getNextStation(int choiceNumber) throws QuestStateException, ChoiceNotPossibleException {

        QuestStateStation questStateStation = null;
        QuestStation questStation = null;
        String choiceId;

        if (choiceNumber == 0) {
            questStateStation = getNextStation("start");
        } else {
            // Get choice
            QuestStation currentStation = this.questPath.peek();
            Choice choice = currentStation.getChoice(this.previousQuestState, choiceNumber - 1);

            // check it was possible
            if (choice == null) {
                throw new ChoiceNotPossibleException(String.format("The choice number %s is not possible.", choiceNumber));
            }

            questStation = choice.getStation();

            if (questStation == null) {
                throw new QuestStateException(String.format("Choice number %s is not found in station '%s'.", choiceNumber, currentStation.getId()));
            }

            questStateStation = processNextStation(questStation);
        }

        return questStateStation;
    }

    public QuestStateStation processNextStation(QuestStation questStation) throws QuestStateException, ChoiceNotPossibleException {
        // resolve back references
        QuestStation nextStation;
        if (questStation.getId().equals(QuestStation.BACK_STATION_ID)) {
            this.questPath.pop();
            nextStation = this.questPath.peek();
        } else {
            nextStation = questStation;
        }

        // update quest state before visit
        this.questState = nextStation.preVisit(this.getQuestState());

        // visit: prepare next station data before updating state (visiting station)
        QuestStateStation retStation = new QuestStateStation();
        retStation.setId(nextStation.getId());
        retStation.setText(nextStation.getText(this.getQuestState()).getValue());
        retStation.setChoices(nextStation.getChoices(this.getQuestState()));

        // save current state to check the choice when selected
        this.previousQuestState = this.getQuestState();

        // update quest state after visit
        this.questState = nextStation.postVisit(this.getQuestState());

        // add to quest path
        this.questPath.push(nextStation);

        // return relevant data for choice
        return retStation;
    }

    protected QuestState getQuestState() {

        if (this.questState == null) {
            this.questState = new QuestState(this.quest.collectAllAttributes());
        }

        return this.questState;
    }

    public Quest getQuest() {
        return quest;
    }
}
