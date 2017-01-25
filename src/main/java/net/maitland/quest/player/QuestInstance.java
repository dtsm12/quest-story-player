package net.maitland.quest.player;

import net.maitland.quest.model.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by David on 19/12/2016.
 */
public class QuestInstance {

    private Quest quest;
    private Deque<QuestStation> questPath = new ArrayDeque<>();
    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    public QuestInstance(Quest quest) {
        this.quest = quest;
    }

    public GameInstance newGameInstance() throws QuestStateException
    {
        return this.getQuest().newGameInstance();
    }

    public QuestStateStation getNextStation(GameInstance gameInstance, String choiceId) throws QuestStateException, ChoiceNotPossibleException {

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
            Choice choice = currentStation.getChoice(gameInstance.getPreviousState(), choiceId);

            // check it was possible
            if (choice == null) {
                throw new ChoiceNotPossibleException(String.format("The choice %s is not possible.", choiceId));
            }

            questStation = choice.getStation();
        }


        if (questStation == null) {
            throw new QuestStateException(String.format("Station %s is not found.", choiceId));
        }

        return processNextStation(gameInstance, questStation);
    }

    public QuestStateStation getNextStation(GameInstance gameInstance, int choiceNumber) throws QuestStateException, ChoiceNotPossibleException {

        QuestStateStation questStateStation = null;
        QuestStation questStation = null;
        String choiceId;

        if (choiceNumber == 0) {
            questStateStation = getNextStation(gameInstance, "start");
        } else {
            // Get choice
            QuestStation currentStation = this.questPath.peek();
            Choice choice = currentStation.getChoice(gameInstance.getPreviousState(), choiceNumber - 1);

            // check it was possible
            if (choice == null) {
                throw new ChoiceNotPossibleException(String.format("The choice number %s is not possible.", choiceNumber));
            }

            questStation = choice.getStation();

            if (questStation == null) {
                throw new QuestStateException(String.format("Choice number %s is not found in station '%s'.", choiceNumber, currentStation.getId()));
            }

            questStateStation = processNextStation(gameInstance, questStation);
        }

        return questStateStation;
    }

    public QuestStateStation processNextStation(GameInstance gameInstance, QuestStation questStation) throws QuestStateException, ChoiceNotPossibleException {

        // resolve back references
        QuestStation nextStation;
        if (questStation.getId().equals(QuestStation.BACK_STATION_ID)) {
            this.questPath.pop();
            nextStation = this.questPath.peek();
        } else {
            nextStation = questStation;
        }

        // update quest state before visit
        nextStation.preVisit(gameInstance);

        // visit: prepare next station data before updating state (visiting station)
        QuestStateStation retStation = new QuestStateStation();
        retStation.setId(nextStation.getId());
        retStation.setText(nextStation.getText(gameInstance.getCurrentState()).getValue());
        retStation.setChoices(getQuestStateChoice(nextStation.getChoices(gameInstance.getCurrentState())));

        // update quest state after visit
        nextStation.postVisit(gameInstance);

        // add to quest path
        this.questPath.push(nextStation);

        // return relevant data for choice
        return retStation;
    }

    protected List<QuestStateChoice> getQuestStateChoice(List<Choice> choices)
    {
        List<QuestStateChoice> questStateChoices = new ArrayList<>();

        for(Choice c : choices)
        {
            QuestStateChoice stateChoice = new QuestStateChoice();
            stateChoice.setStationId(c.getStation().getId());
            stateChoice.setText(c.getText());
            questStateChoices.add(stateChoice);
        }

        return questStateChoices;
    }

    public Quest getQuest() {
        return quest;
    }
}
