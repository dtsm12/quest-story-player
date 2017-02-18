package net.maitland.quest.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by David on 23/01/2017.
 */
public class Game {


    public static Game fromCollectionStructure(Map gameData) throws Exception {
        Game game = null;

        if (gameData != null) {

            List<String> questPath = (List<String>) gameData.get("questPath");
            Map<String, Map<String, Map<String, String>>> currentState = (Map<String, Map<String, Map<String, String>>>) gameData.get("currentState");
            Map<String, Map<String, Map<String, String>>> previousState = (Map<String, Map<String, Map<String, String>>>) gameData.get("previousState");
            Map<String, String> about = (Map<String, String>) gameData.get("gameQuest");

            game = new Game(new About(about.get("title"), about.get("author")));
            game.getGameQuest().setIntro(about.get("intro"));
            game.setChoiceIndex((Integer)gameData.get("choiceIndex"));
            game.setChoiceId((String)gameData.get("choiceId"));
            game.setQuestPath(new ArrayDeque<String>(questPath));
            game.setQuestState(new QuestState(getAttributes(currentState.get("attributes"))));
            game.setPreviousQuestState(new QuestState(getAttributes((previousState).get("attributes"))));

        }

        return game;
    }

    public static Map<String, Attribute> getAttributes(Map<String, Map<String, String>> rawAttributes) throws Exception{

        Map<String, Attribute> attributes = new HashMap();

        for (String name : rawAttributes.keySet())
        {
            Map<String, String> rawAttr = rawAttributes.get(name);

            Attribute attr = (Attribute) Class.forName(rawAttr.get("type")).newInstance();
            attr.setName(name);
            attr.setValue(rawAttr.get("value"));

            attributes.put(name, attr);
        }

        return attributes;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private About gameQuest;
    private QuestState questState = new QuestState();
    private QuestState previousQuestState = new QuestState();
    private Deque<String> questPath = new ArrayDeque<>();

    private Integer choiceIndex;
    private String choiceId;

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    /* Use default/package access modifier - should be obtained from Quest */
    Game(About gameQuest) {
        this.gameQuest = gameQuest;
    }

    public Integer getChoiceIndex() {
        return choiceIndex;
    }

    public void setChoiceIndex(Integer choiceIndex) {
        this.choiceIndex = choiceIndex;
    }

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public About getGameQuest() {
        return gameQuest;
    }

    public QuestState getCurrentState() {
        return this.questState;
    }

    public QuestState getPreviousState() {
        return this.previousQuestState;
    }

    public void updateState(Collection<Attribute> attributes) throws QuestStateException {
        QuestState newState = this.questState.copyAttributes();

        for (Attribute a : attributes) {
            //evaluate the attributes value
            String attrValue = expressionEvaluator.evaluateExpression(a, newState);

            // check it's the right type of value
            if (a.isValidValue(attrValue) == false) {
                throw new QuestStateException(String.format("Attribute '%s' expression '%s' evaluates to incorrect type", a.getName(), attrValue));
            }

            log.debug("Setting attribute '{}' to value '{}'", a.getName(), attrValue);

            // update the Quest's state
            newState.put(a.updateValue(attrValue));
        }

        this.previousQuestState = this.questState;
        this.questState = newState;
    }

    public Deque<String> getQuestPath() {
        return questPath;
    }

    protected void setQuestState(QuestState questState) {
        this.questState = questState;
    }

    protected void setPreviousQuestState(QuestState previousQuestState) {
        this.previousQuestState = previousQuestState;
    }

    protected void setQuestPath(Deque<String> questPath) {
        this.questPath = questPath;
    }

    protected void setGameQuest(About gameQuest) {
        this.gameQuest = gameQuest;
    }

    public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }
}
