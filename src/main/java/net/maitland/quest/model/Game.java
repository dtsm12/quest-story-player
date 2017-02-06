package net.maitland.quest.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 23/01/2017.
 */
public class Game {

    public static Game fromCollectionStructure(Map gameData) throws Exception {
        Game game = null;

        if (gameData != null) {
            List<String> questPath = (List<String>) gameData.get("questPath");
            Map<String, Map<String, String>> currentState = (Map<String, Map<String, String>>) gameData.get("currentState");
            Map<String, Map<String, String>> previousState = (Map<String, Map<String, String>>) gameData.get("previousState");
            Map<String, String> about = (Map<String, String>) gameData.get("gameQuest");

            game.setGameQuest(new About(about.get("title"), about.get("author")));
            game.setQuestPath(new ArrayDeque<String>(questPath));
            game.setQuestState(new QuestState(currentState.get("attributes")));
            game.setPreviousQuestState(new QuestState((previousState).get("attributes")));
        }

        return game;
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

    public void updateState(List<Attribute> attributes) throws QuestStateException {
        Map<String, String> newState = this.questState.copyAttributes();

        for (Attribute a : attributes) {
            //evaluate the attributes value
            String attrValue = expressionEvaluator.evaluateExpression(a, newState);

            // check it's the right type of value
            if (a.isValidValue(attrValue) == false) {
                throw new QuestStateException(String.format("Attribute '%s' expression '%s' evaluates to incorrect type", a.getName(), a.getValue()));
            }

            log.debug("Setting attribute '{}' to value '{}'", a.getName(), attrValue);

            // update the Quest's state
            newState.put(a.getName(), attrValue);
        }

        this.previousQuestState = this.questState;
        this.questState = new QuestState(newState);
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
