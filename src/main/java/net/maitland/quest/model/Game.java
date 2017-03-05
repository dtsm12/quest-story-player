package net.maitland.quest.model;

import net.maitland.quest.model.attribute.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by David on 23/01/2017.
 */
public class Game {

    public static final String QML_START_TIME = "qmlStartTime";

    public static Game fromCollectionStructure(Map gameData) throws Exception {
        Game game = null;

        if (gameData != null) {

            List<String> questPath = (List<String>) gameData.get("questPath");
            Map<String, Map<String, String>> attributes = (Map<String, Map<String, String>>) gameData.get("attributes");
            Map<String, String> about = (Map<String, String>) gameData.get("gameQuest");

            game = new Game(new About(about.get("title"), about.get("author")), getAttributes(attributes));
            game.getGameQuest().setIntro(about.get("intro"));
            game.setChoiceIndex((Integer) gameData.get("choiceIndex"));
            game.setChoiceId((String) gameData.get("choiceId"));
            game.setQuestPath(new ArrayDeque<String>(questPath));

        }

        return game;
    }

    public static Map<String, Attribute> getAttributes(Map<String, Map<String, String>> rawAttributes) throws Exception {

        Map<String, Attribute> attributes = new HashMap();

        for (String name : rawAttributes.keySet()) {
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
    private Deque<String> questPath = new ArrayDeque<>();

    private Map<String, Attribute> attributes = new HashMap<>();

    private Integer choiceIndex;
    private String choiceId;

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    /* Use default/package access modifier - should be obtained from Quest */
    public Game(About gameQuest) {
        this(gameQuest, null);
    }

    /* Use default/package access modifier - should be obtained from Quest */
    public Game(About gameQuest, Map<String, Attribute> attributes) {

        this.gameQuest = gameQuest;

        if (attributes != null) {
            this.attributes = new HashMap<>(attributes);
        } else {
            this.attributes = new HashMap<>();
        }

        // add quest start time if missing
        if(this.attributes.containsKey(QML_START_TIME) == false) {
            this.put(new StringAttribute(QML_START_TIME, String.valueOf((new Date()).getTime())));
        }

        // add/over-write built-in attributes
        this.put(new OperatorAttribute(" greater ", " > "));
        this.put(new OperatorAttribute(" above ", " > "));
        this.put(new OperatorAttribute(" lower ", " < "));
        this.put(new OperatorAttribute(" below ", " < "));
        this.put(new OperatorAttribute(" and ", " && "));
        this.put(new OperatorAttribute(" or ", " || "));
        this.put(new OperatorAttribute("not ", "! "));
        this.put(new OperatorAttribute(" = ", " == "));
        this.put(new OperatorAttribute(" equal ", " == "));
        this.put(new RandomFunctionAttribute());
        this.put(new StatesFunctionAttribute());
        this.put(new TemplateAttribute("{contains ([^{}]+), ([^{}]+)}", "(%1$s.indexOf(%2$s) > -1)"));
        this.put(new TemplateAttribute("{containsWord ([^{}]+), '([\\s\\w]+)'}", "(%1$s.search(/\\b%2$s\\b/) > -1)"));
        this.put(new TemplateAttribute("{verbose (\\d+)}", "'%1$s'.getOrdinal()"));
        this.put(new TemplateAttribute("{upper ([^{}]+)}", "%1$s.toUpperCase()"));
        this.put(new TemplateAttribute("{lower ([^{}]+)}", "%1$s.toLowerCase()"));
        this.put(new TemplateAttribute("{repeatString ([^{}]+), (\\d)}", "%1$s.repeat(%2$s)"));
        this.put(new VisitsFunctionAttribute());
        this.put(new QmlMinutesAttribute());
        this.put(new QmlSecondsAttribute());
        this.put(new QmlDayAttribute());
        this.put(new QmlTimeAttribute());
        this.put(new QmlServerAttribute());
        this.put(new QmlVersionAttribute());
        this.put(new QmlStationAttribute());
        this.put(new QmlLastStationAttribute());
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

    public void put(Attribute attribute) {
        this.attributes.put(attribute.getName(), attribute);
    }

    public Map<String, Attribute> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    public String getAttributeValue(String name) {
        return this.attributes.get(name).getValue();
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public String replace(String value, boolean isCheck) {

        for (Attribute a : this.attributes.values()) {
            value = a.replace(value, this, isCheck);
        }

        return value;
    }

    public boolean check(Choice choice) throws QuestStateException {

        return expressionEvaluator.check(choice, this);
    }

    public boolean check(IfSection ifSection) throws QuestStateException {

        return expressionEvaluator.check(ifSection, this);
    }

    public String toStateText(String text) throws QuestStateException {

        return this.replace(text, false);
    }

    public void updateState(Collection<Attribute> attributes) throws QuestStateException {

        for (Attribute a : attributes) {
            //evaluate the attributes value
            String attrValue = expressionEvaluator.evaluateExpression(a, this, false);

            // check it's the right type of value
            if (a.isValidValue(attrValue) == false) {
                throw new QuestStateException(String.format("Attribute '%s' expression '%s' evaluates to incorrect type", a.getName(), attrValue));
            }

            log.debug("Setting attribute '{}' to value '{}'", a.getName(), attrValue);

            // update the Quest's state
            this.attributes.put(a.getName(), a.updateValue(attrValue));
        }
    }

    public Deque<String> getQuestPath() {
        return questPath;
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
