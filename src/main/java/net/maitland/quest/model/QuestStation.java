package net.maitland.quest.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
public class QuestStation extends QuestSection  {

    public static final String START_STATION_ID = "start";
    public static final String BACK_STATION_ID = "back";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
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

    public void preVisit(Game game) throws QuestStateException
    {
        this.visit(game, false);
    }

    public void postVisit(Game game) throws QuestStateException
    {
        this.visit(game, true);
    }

    protected void visit(Game game, boolean postVisit) throws QuestStateException
    {
        // Get this Station's attributes
        List<Attribute> questAttributes = new ArrayList(postVisit ? this.getPostVisitAttributes() : this.getPreVisitAttributes());

        // if applicable section isn't the station itself add in that section's attributes
        QuestSection applicableQuestSection = getApplicableQuestSection(game.getCurrentState());
        if (this != applicableQuestSection) {
            questAttributes.addAll(postVisit ? applicableQuestSection.getPostVisitAttributes() : applicableQuestSection.getPreVisitAttributes());
        }

        game.updateState(questAttributes);
    }

    public Text getText(QuestState questState) throws QuestStateException {


        String text = getApplicableQuestSection(questState).getText().getValue();

        List<String> attributeNames = this.expressionEvaluator.extractAttributeNames(text);

        for (String a : attributeNames) {
            text = text.replace(a, questState.getAttributeValue(a));
        }

        return new Text(text);
    }

    public List<Choice> getChoices(QuestState questState) throws QuestStateException {

        QuestSection questSection = getApplicableQuestSection(questState);
        List<Choice> filteredChoices = new ArrayList<>();

        for (Choice c : questSection.getChoices()) {
            if (expressionEvaluator.check(c, questState.copyAttributes())) {
                filteredChoices.add(c);
            }
        }

        return filteredChoices;
    }

    public Choice getChoice(QuestState questState, String choiceId) throws QuestStateException
    {
        Choice choice = null;

        for(Choice c: getChoices(questState))
        {
            if(c.getStation().getId().equals(choiceId))
            {
                choice = c;
                break;
            }
        }

        return choice;
    }

    public Choice getChoice(QuestState questState, int choiceIndex) throws QuestStateException
    {
        Choice choice = null;

        List<Choice> choices = getChoices(questState);

        return choices.get(choiceIndex);
    }

    protected QuestSection getApplicableQuestSection(QuestState questState) throws QuestStateException {

        QuestSection questSection = null;

        if (this.getConditions().size() > 0) {

            for (IfSection i : this.getConditions()) {
                if (expressionEvaluator.check(i, questState.copyAttributes())) {
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

