
/**
 * Created by David on 04/12/2016.
 */
package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import net.maitland.quest.player.ChoiceNotPossibleException;
import net.maitland.quest.player.QuestStateChoice;
import net.maitland.quest.player.QuestStateException;
import net.maitland.quest.player.QuestStateStation;

import java.util.*;
import java.util.stream.Collectors;

public final class Quest {

    private About about;

    @JacksonXmlProperty(localName = "station")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<QuestStation> stations = new ArrayList<>();

    public GameInstance newGameInstance() throws QuestStateException {
        GameInstance gameInstance = new GameInstance();
        gameInstance.updateState(this.collectAllAttributes());
        return gameInstance;
    }

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    public List<QuestStation> getStations() {
        return stations;
    }

    public void setStations(List<QuestStation> stations) {
        this.stations = stations;
    }

    public void addStation(QuestStation questStation) {
        this.stations.add(questStation);
    }

    public QuestStation getStation(String stationId) {
        return this.stations.stream().filter(s -> s.getId().equals(stationId))
                .findFirst()
                .get();
    }

    protected void collectAllAttributes(QuestSection questSection, List<Attribute> attributes) {

        if (questSection != null) {
            /* create attributes with default values */
            questSection.getNumberAttributes().stream().forEach(n -> attributes.add(new NumberAttribute(n.getName())));
            questSection.getStateAttributes().stream().forEach(st -> attributes.add(new StateAttribute(st.getName())));
        }
    }

    protected void collectAllAttributes(QuestStation s, List<Attribute> attributes) {

        // collect station's attributes
        collectAllAttributes((QuestSection) s, attributes);

        // collect all if condition attributes
        s.getConditions().stream().forEach(is -> collectAllAttributes(is, attributes));

        // collect else condition attributes
        collectAllAttributes(s.getElseCondition(), attributes);
    }

    protected List<Attribute> collectAllAttributes() {

        List<Attribute> attributes = new ArrayList<>();
        this.stations.stream().forEach(qs -> collectAllAttributes(qs, attributes));
        return attributes;
    }

    public QuestStateStation getNextStation(GameInstance gameInstance, String choiceId) throws QuestStateException, ChoiceNotPossibleException {

        QuestStation questStation = null;

        if (choiceId == null) {
            choiceId = "start";
        }

        if (gameInstance.getQuestPath().isEmpty()) {

            questStation = getStation(choiceId);

        } else {
            // Get choice
            String currentStationId = gameInstance.getQuestPath().peek();
            QuestStation currentStation = this.getStation(currentStationId);
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
            String currentStationId = gameInstance.getQuestPath().peek();
            QuestStation currentStation = this.getStation(currentStationId);
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

    protected QuestStateStation processNextStation(GameInstance gameInstance, QuestStation questStation) throws QuestStateException, ChoiceNotPossibleException {

        // resolve back references
        QuestStation nextStation;
        Deque<String> questPath = gameInstance.getQuestPath();

        if (questStation.getId().equals(QuestStation.BACK_STATION_ID)) {
            questPath.pop();
            String nextStationId = questPath.peek();
            nextStation = this.getStation(nextStationId);
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
        questPath.push(nextStation.getId());

        // return relevant data for choice
        return retStation;
    }

    protected List<QuestStateChoice> getQuestStateChoice(List<Choice> choices) {

        List<QuestStateChoice> questStateChoices = choices.stream().map(choice -> newQuestStateChoice(choice)).collect(Collectors.toList());
        return questStateChoices;
    }

    private QuestStateChoice newQuestStateChoice(Choice c) {
        QuestStateChoice stateChoice = new QuestStateChoice();
        stateChoice.setStationId(c.getStation().getId());
        stateChoice.setText(c.getText());
        return stateChoice;
    }
}
