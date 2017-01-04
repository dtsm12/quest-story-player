package net.maitland.quest.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.maitland.quest.jackson.BackStationAwareObjectIdResolver;
import net.maitland.quest.player.ExpressionEvaluator;
import net.maitland.quest.player.QuestInstance;
import net.maitland.quest.player.QuestStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 18/12/2016.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                    property = "id",
                    resolver = BackStationAwareObjectIdResolver.class)
public class QuestStation extends QuestSection  {

    public static final String BACK_STATION_ID = "back";
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

    public QuestState visit(QuestState questState) throws QuestStateException
    {
        Map<String, String> newAttributes = getUpdatedAttributes(questState);

        return new QuestState(newAttributes);
    }

    protected Map<String, String> getUpdatedAttributes(QuestState questState) throws QuestStateException {

        Map<String, String> newAttributes = new HashMap<>(questState.getAttributes());

        // process this Station's attributes
        updateAttributesWithQuestSection(this, newAttributes);

        // if applicable section isn't the station itself
        QuestSection applicableQuestSection = getApplicableQuestSection(questState);
        if (this != applicableQuestSection) {
            updateAttributesWithQuestSection(applicableQuestSection, newAttributes);
        }

        return newAttributes;
    }

    protected void updateAttributesWithQuestSection(QuestSection questSection, Map<String, String> attributes) throws QuestStateException {
        for (Attribute a : questSection.getAttributes()) {
            //evaluate the attributes value
            String attrValue = expressionEvaluator.evaluateExpression(a, attributes);

            // check it's the right type of value
            if (a.isValidValue(attrValue) == false) {
                throw new QuestStateException(String.format("Attribute '%s' expression '%s' evaluates to incorrect type", a.getName(), a.getValue()));
            }

            // update the Quest's state
            attributes.put(a.getName(), attrValue);
        }
    }

    public Text getText(QuestState questState) throws QuestStateException {


        String text = getApplicableQuestSection(questState).getText().getValue();

        List<String> attributeNames = this.expressionEvaluator.extractAttributeNames(text);

        for (String a : attributeNames) {
            text = text.replace(a, questState.getAttributes().get(a));
        }

        return new Text(text);
    }

    public List<Choice> getChoices(QuestState questState) throws QuestStateException {

        QuestSection questSection = getApplicableQuestSection(questState);
        List<Choice> filteredChoices = new ArrayList<>();

        for (Choice c : questSection.getChoices()) {
            if (expressionEvaluator.check(c, questState.getAttributes())) {
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

    protected QuestSection getApplicableQuestSection(QuestState questState) throws QuestStateException {

        QuestSection questSection = null;

        if (this.getConditions().size() > 0) {

            for (IfSection i : this.getConditions()) {
                if (expressionEvaluator.check(i, questState.getAttributes())) {
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

        return questSection;
    }
}

