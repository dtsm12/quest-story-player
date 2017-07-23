package net.maitland.quest.model;

import net.maitland.quest.model.attribute.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by David on 16/03/2017.
 */
public class VisitingStation {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private QuestStation questStation;
    private Game game = null;
    private List<Text> texts = new ArrayList<>();
    private List<Attribute> mainStationPreVisitAttributes = new ArrayList<>();
    private List<Attribute> mainStationPostVisitAttributes = new ArrayList<>();
    private List<QuestSectionConditionSet> conditionalSets = new ArrayList<>();
    private List<Choice> choices = new ArrayList<>();

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    private boolean isExclusive = false;

    public VisitingStation(QuestStation questStation, Game game) {
        this.questStation = questStation;
        this.game = game;

        apply(questStation, null);

        //add any applicable includes
        questStation.getIncludedStations().stream().filter(is -> expressionEvaluator.check(is, game, game.getAttributes())).forEach(is -> apply(is));
    }

    public List<GameChoice> getChoices() {
        return getChoices(this.getConditionalState(), getPreVisitState());
    }

    public List<GameChoice> getChoices(Map<String, Attribute> conditionalState, Map<String, Attribute> attributesState)

    {
        List<Choice> applicableChoices = new ArrayList<>(this.choices);

        // pick up text from conditional sections
        for (QuestSectionConditionSet conditionSet : this.getQuestSectionConditionSets()) {
            QuestSection questSection = conditionSet.getApplicableCondition(this.game, conditionalState, this.expressionEvaluator);
            if (questSection != null) {
                applicableChoices.addAll(questSection.getChoices());
            }
        }

        return expressionEvaluator.getQuestStateChoices(applicableChoices, this.game, attributesState);
    }

    public String getText() {
        return getText(this.getConditionalState(), this.getPreVisitState());
    }

    public String getText(Map<String, Attribute> conditionalState, Map<String, Attribute> attributesState) {
        return expressionEvaluator.toStateText(this.getTexts(conditionalState), this.game, attributesState);
    }

    public List<Text> getTexts(Map<String, Attribute> conditionalState) {

        List<Text> applicableTexts = new ArrayList<>(this.texts);

        // pick up text from conditional sections
        for (QuestSectionConditionSet conditionSet : this.getQuestSectionConditionSets()) {
            QuestSection questSection = conditionSet.getApplicableCondition(this.game, conditionalState, this.expressionEvaluator);
            if (questSection != null) {
                applicableTexts.addAll(questSection.getTexts());
            }
        }

        return applicableTexts;
    }

    public List<QuestSectionConditionSet> getQuestSectionConditionSets() {
        return this.conditionalSets;
    }


    public GameStation visit() throws QuestStateException {
        log.debug("Pre-visit '{}'", this.questStation.getId());
        Map<String, Attribute> conditionalState = getConditionalState();
        Map<String, Attribute> preVisitState = getPreVisitState();

        log.debug("Visiting '{}'", this.questStation.getId());
        GameStation retStation = new GameStation();
        retStation.setId(this.questStation.getId());
        retStation.setText(getText(conditionalState, preVisitState));
        retStation.setChoices(getChoices(conditionalState, preVisitState));
        return retStation;
    }

    /**
     * Get the state which should be used to determine applicable states
     *
     * @return
     */
    protected Map<String, Attribute> getConditionalState() {
        if (this.game.getConditionalState().size() == 0) {
            Map<String, Attribute> conditionalState = this.game.copyAttributes();
            expressionEvaluator.updateState(game, conditionalState, this.mainStationPreVisitAttributes);
            this.game.setConditionalState(conditionalState);
        }
        return this.game.getConditionalState();
    }

    protected Map<String, Attribute> getPreVisitState() {
        if (this.game.getPreVisitState().size() == 0) {
            Map<String, Attribute> preVisitState = this.game.copyAttributes();
            this.processVisitSequence(this.game, preVisitState, false);
            this.game.setPreVisitState(preVisitState);
        }
        return this.game.getPreVisitState();
    }

    public void leave() throws QuestStateException {

        Map<String, Attribute> state = getPreVisitState();
        log.debug("Post-visit '{}'", this.questStation.getId());
        this.processVisitSequence(this.game, state, true);
        game.setAttributes(state);
    }

    protected void processVisitSequence(Game game, Map<String, Attribute> state, boolean postVisit) throws QuestStateException {
        // apply main station attributes
        log.debug("{}-visit applying main station '{}' attributes", postVisit ? "Post" : "Pre", this.questStation.getId());

        // apply pre-visit main station attributes
        if (postVisit == false) {
            expressionEvaluator.updateState(game, state, this.mainStationPreVisitAttributes);
        }

        // apply any any conditional attributes
        for (QuestSectionConditionSet conditionSet : this.getQuestSectionConditionSets()) {
            QuestSection questSection = conditionSet.getApplicableCondition(game, getConditionalState(), this.expressionEvaluator);
            if (questSection != null) {
                log.debug("{}-visit applying station '{}' conditional attributes", postVisit ? "Post" : "Pre", this.questStation.getId());
                if (postVisit) {
                    expressionEvaluator.updateState(game, state, questSection.getPostVisitAttributes());
                } else {
                    expressionEvaluator.updateState(game, state, questSection.getPreVisitAttributes());
                }
            }
        }

        if (postVisit) {
            expressionEvaluator.updateState(game, state, this.mainStationPostVisitAttributes);
        }
    }

    protected void apply(IncludedStation includedStation) {
        apply(includedStation.getStation(), includedStation.getProcess());
    }

    protected void apply(QuestSection qs, IncludeProcess insertLocation) {
        if (this.isExclusive == false) {
            addToList(this.texts, qs.getTexts(), insertLocation);
            addToList(this.conditionalSets, qs, insertLocation);
            addToList(this.mainStationPreVisitAttributes, qs.getPreVisitAttributes(), insertLocation);
            addToList(this.mainStationPostVisitAttributes, qs.getPostVisitAttributes(), insertLocation);
            addToList(this.choices, qs.getChoices(), insertLocation);
        }
        if (insertLocation != null && IncludeProcess.exclusive.equals(insertLocation)) {
            this.isExclusive = true;
        }
    }

    protected void addToList(List superList, List subList, IncludeProcess insertLocation) {

        int insertPosition = 0;
        if (insertLocation == null || IncludeProcess.after.equals(insertLocation)) {
            insertPosition = superList.size();
        }
        if (insertLocation != null && IncludeProcess.exclusive.equals(insertLocation)) {
            superList.clear();
        }
        superList.addAll(insertPosition, subList);
    }

    protected void addToList(List<QuestSectionConditionSet> superList, QuestSectionConditionSet conditionSet, IncludeProcess insertLocation) {

        int insertPosition = 0;
        if (insertLocation == null || IncludeProcess.after.equals(insertLocation)) {
            insertPosition = superList.size();
        }
        if (insertLocation != null && IncludeProcess.exclusive.equals(insertLocation)) {
            superList.clear();
        }
        superList.add(insertPosition, conditionSet);
    }
}
