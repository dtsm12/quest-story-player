package net.maitland.quest.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.maitland.quest.model.attribute.Attribute;
import net.maitland.quest.parser.jackson.BackStationAwareObjectIdResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 18/12/2016.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        resolver = BackStationAwareObjectIdResolver.class)
public class QuestStation extends QuestSection {

    public static final String START_STATION_ID = "start";
    public static final String BACK_STATION_ID = "back";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static QuestStation backStation;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Text visit() throws QuestStateException {
        return getText();
    }

    public static QuestStation getBackStation() {
        if (backStation == null) {
            backStation = new QuestStation();
            backStation.setId(BACK_STATION_ID);
        }
        return backStation;
    }

    public void preVisit(Game game) throws QuestStateException {
        this.visit(game, false);
    }

    public void postVisit(Game game) throws QuestStateException {
        this.visit(game, true);
    }

    protected void visit(Game game, boolean postVisit) throws QuestStateException {
        // Get this Station's attributes
        List<Attribute> questAttributes = new ArrayList(postVisit ? this.getPostVisitAttributes() : this.getPreVisitAttributes());

        // if applicable section isn't the station itself add in that section's attributes
        QuestSection applicableQuestSection = getApplicableQuestSection(game);
        if (this != applicableQuestSection) {
            questAttributes.addAll(postVisit ? applicableQuestSection.getPostVisitAttributes() : applicableQuestSection.getPreVisitAttributes());
        }

        game.updateState(questAttributes);
    }

    public Text getText(Game game) throws QuestStateException {

        String text = getApplicableQuestSection(game).getText().getValue();
        return new Text(game.toStateText(text));
    }

    public List<Choice> getChoices(Game game) throws QuestStateException {

        QuestSection questSection = getApplicableQuestSection(game);
        List<Choice> filteredChoices = new ArrayList<>();

        for (Choice c : questSection.getChoices()) {
            if (game.check(c)) {
                filteredChoices.add(c);
            }
        }

        return filteredChoices;
    }

    public Choice getChoice(Game game, String choiceId) throws QuestStateException {
        Choice choice = null;

        for (Choice c : getChoices(game)) {
            if (c.getStationId().equals(choiceId)) {
                choice = c;
                break;
            }
        }

        return choice;
    }

    public Choice getChoice(Game game, int choiceIndex) throws QuestStateException {
        Choice choice = null;

        List<Choice> choices = getChoices(game);

        return choices.get(choiceIndex);
    }

    protected QuestSection getApplicableQuestSection(Game game) throws QuestStateException {

        QuestSection questSection = null;

        if (this.getConditions().size() > 0) {

            for (IfSection i : this.getConditions()) {
                if (game.check(i)) {
                    questSection = i;
                    break;
                }
            }

            if (questSection == null) {
                questSection = this.getElseCondition();
            }
        }

        if (questSection == null) {
            questSection = this;
        }

        this.log.debug("Applicable section for stationId '{}' is {}", this.getId(), questSection.getClass().getName());

        return questSection;
    }
}

