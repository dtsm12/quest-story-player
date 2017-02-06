
/**
 * Created by David on 04/12/2016.
 */
package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import net.maitland.quest.player.ChoiceNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Quest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private About about;

    @JacksonXmlProperty(localName = "station")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<QuestStation> stations = new ArrayList<>();

    public Game newGameInstance() throws QuestStateException {
        Game game = new Game(this.about);
        game.updateState(this.collectAllAttributes());
        return game;
    }

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    public List<QuestStation> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public void setStations(List<QuestStation> stations) {
        this.stations = stations;
    }

    public void addStation(QuestStation questStation) {
        this.stations.add(questStation);
    }

    public GameStation getNextStation(Game game) throws QuestStateException, ChoiceNotPossibleException {
        GameStation gameStation = null;

        if ((game.getChoiceIndex() == null && game.getChoiceId() == null) ||
                (game.getChoiceIndex() != null && game.getChoiceId() != null)) {
            throw new QuestStateException("choiceIndex or choiceId must be specified, not both.");
        }

        if (game.getChoiceIndex() != null) {
            gameStation = getNextStation(game, game.getChoiceIndex());
        }

        if (game.getChoiceId() != null) {
            gameStation = getNextStation(game, game.getChoiceId());
        }

        return gameStation;
    }

    protected void collectAllAttributes(QuestSection questSection, List<Attribute> attributes) {

        if (questSection != null) {
            /* create attributes with default values */
            questSection.getNumberAttributes().stream().forEach(n -> attributes.add(new NumberAttribute(n.getName())));
            questSection.getStateAttributes().stream().forEach(st -> attributes.add(new StateAttribute(st.getName())));
            questSection.getStringAttributes().stream().forEach(st -> attributes.add(new StringAttribute(st.getName())));
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

    protected QuestStation getStation(String stationId) {
        return this.stations.stream().filter(s -> s.getId().equals(stationId))
                .findFirst()
                .get();
    }

    protected GameStation getNextStation(Game game, String choiceId) throws QuestStateException, ChoiceNotPossibleException {

        QuestStation questStation = null;

        if (choiceId == null) {
            choiceId = QuestStation.START_STATION_ID;
        }

        if (game.getQuestPath().isEmpty()) {

            questStation = getStation(choiceId);

        } else {
            // Get choice
            String currentStationId = game.getQuestPath().peek();
            QuestStation currentStation = this.getStation(currentStationId);
            Choice choice = currentStation.getChoice(game.getPreviousState(), choiceId);

            // check it was possible
            if (choice == null) {
                throw new ChoiceNotPossibleException(String.format("The choice %s is not possible.", choiceId));
            }

            questStation = choice.getStation();
        }


        if (questStation == null) {
            throw new QuestStateException(String.format("Station %s is not found.", choiceId));
        }

        return processNextStation(game, questStation);
    }

    protected GameStation getNextStation(Game game, int choiceNumber) throws QuestStateException, ChoiceNotPossibleException {

        GameStation questStateStation = null;
        QuestStation questStation = null;
        String choiceId;

        if (choiceNumber == 0) {
            questStateStation = getNextStation(game, QuestStation.START_STATION_ID);
        } else {
            // Get choice
            String currentStationId = game.getQuestPath().peek();
            QuestStation currentStation = this.getStation(currentStationId);
            Choice choice = currentStation.getChoice(game.getPreviousState(), choiceNumber - 1);

            // check it was possible
            if (choice == null) {
                throw new ChoiceNotPossibleException(String.format("The choice number %s is not possible.", choiceNumber));
            }

            questStation = choice.getStation();

            if (questStation == null) {
                throw new QuestStateException(String.format("Choice number %s is not found in station '%s'.", choiceNumber, currentStation.getId()));
            }

            questStateStation = processNextStation(game, questStation);
        }

        return questStateStation;
    }

    protected GameStation processNextStation(Game game, QuestStation questStation) throws QuestStateException, ChoiceNotPossibleException {

        // resolve back references
        QuestStation nextStation = null;
        Deque<String> questPath = game.getQuestPath();
        List<Choice> choices = null;

        if (questStation.getId().equals(QuestStation.BACK_STATION_ID)) {

            //remove last station
            questPath.pop();

            String nextStationId = questPath.peek();
            nextStation = this.getStation(nextStationId);

        } else {
            nextStation = questStation;
        }

        // update quest state before visit
        log.debug("Pre-visit '{}'", nextStation.getId());
        nextStation.preVisit(game);

        // visit: prepare next station data before updating state (visiting station)
        log.debug("Visit '{}'", nextStation.getId());
        GameStation retStation = new GameStation();
        retStation.setId(nextStation.getId());
        retStation.setText(nextStation.getText(game.getCurrentState()).getValue());
        retStation.setChoices(getQuestStateChoice(nextStation.getChoices(game.getCurrentState())));

        // update quest state after visit
        log.debug("Post-visit '{}'", nextStation.getId());
        nextStation.postVisit(game);

        // add to quest path
        questPath.push(nextStation.getId());

        // return relevant data for choice
        return retStation;
    }

    protected List<GameChoice> getQuestStateChoice(List<Choice> choices) {

        List<GameChoice> gameChoices = choices.stream().map(choice -> newQuestStateChoice(choice)).collect(Collectors.toList());
        return gameChoices;
    }

    protected GameChoice newQuestStateChoice(Choice c) {
        GameChoice stateChoice = new GameChoice();
        stateChoice.setStationId(c.getStation().getId());
        stateChoice.setText(c.getText());
        return stateChoice;
    }
}
