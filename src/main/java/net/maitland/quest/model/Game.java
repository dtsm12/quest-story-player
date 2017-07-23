package net.maitland.quest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.maitland.quest.model.attribute.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by David on 23/01/2017.
 */
public class Game {

    public static final String QML_START_TIME = "qmlStartTime";

    public static Game fromCollectionStructure(Map gameData) throws Exception {
        Game game = null;

        if (gameData != null) {
            try {
                List<String> questPath = (List<String>) gameData.get("questPath");
                Map<String, String> about = (Map<String, String>) gameData.get("gameQuest");
                Map<String, Map<String, String>> attributes = (Map<String, Map<String, String>>) gameData.get("attributes");
                Map<String, Map<String, String>> conditionalState = (Map<String, Map<String, String>>) gameData.get("conditionalState");
                Map<String, Map<String, String>> preVisitState = (Map<String, Map<String, String>>) gameData.get("preVisitState");

                game = new Game(new About(about.get("title"), about.get("author")), getAttributes(attributes), getAttributes(conditionalState), getAttributes(preVisitState));
                game.getGameQuest().setIntro(about.get("intro"));
                Object choiceIndex = gameData.get("choiceIndex");
                if (choiceIndex != null) {
                    game.setChoiceIndex(Integer.parseInt(choiceIndex.toString()));
                }
                game.setChoiceId((String) gameData.get("choiceId"));
                game.setQuestPath(new ArrayDeque<String>(questPath));
            } catch (Exception e) {
                ObjectMapper om = new ObjectMapper();
                throw new QuestStateException("Error parsing quest: " + om.writeValueAsString(gameData), e);
            }
        }

        return game;
    }

    public static Map<String, Attribute> getAttributes(Map<String, Map<String, String>> rawAttributes) throws Exception {

        Map<String, Attribute> attributes = new HashMap();

        for (String name : rawAttributes.keySet()) {
            Map<String, String> rawAttr = rawAttributes.get(name);

            Attribute attr = (Attribute) Class.forName(rawAttr.get("type")).newInstance();
            for (String propertyName : rawAttr.keySet()) {
                if (propertyName.equals("type") == false) {
                    PropertyUtils.setSimpleProperty(attr, propertyName, rawAttr.get(propertyName));
                }
            }

            attributes.put(name, attr);
        }

        return attributes;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private About gameQuest;
    private Deque<String> questPath = new ArrayDeque<>();

    private Map<String, Attribute> attributes = new HashMap<>();
    private Map<String, Attribute> conditionalState = new HashMap<>();
    private Map<String, Attribute> preVisitState = new HashMap<>();

    private Integer choiceIndex;
    private String choiceId;

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    public Game() {
        this(null, null, null, null);
    }

    public Game(About gameQuest) {
        this(gameQuest, null, null, null);
    }

    public Game(About gameQuest, Map<String, Attribute> attributes) {
        this(gameQuest, attributes, null, null);
    }

    public Game(About gameQuest, Map<String, Attribute> attributes, Map<String, Attribute> conditionalState, Map<String, Attribute> preVisitState) {

        this.gameQuest = gameQuest;

        if (attributes != null) {
            this.attributes = getCleanCopy(attributes);
        } else {
            this.attributes = getCleanCopy(null);
        }

        if (conditionalState != null) {
            this.conditionalState = getCleanCopy(conditionalState);
        } else {
            this.conditionalState = getCleanCopy(null);
        }

        if (preVisitState != null) {
            this.preVisitState = getCleanCopy(preVisitState);
        } else {
            this.preVisitState = getCleanCopy(null);
        }
    }

    protected void cleanAttributes(Map<String, Attribute> attributes) {
        // add quest start time if missing
        if (attributes.containsKey(QML_START_TIME) == false) {
            this.put(attributes, new StringAttribute(QML_START_TIME, String.valueOf((new Date()).getTime())));
        }
        // add/over-write built-in attributes
        this.put(attributes, new OperatorAttribute(" greater ", " > "));
        this.put(attributes, new OperatorAttribute(" above ", " > "));
        this.put(attributes, new OperatorAttribute(" lower ", " < "));
        this.put(attributes, new OperatorAttribute(" below ", " < "));
        this.put(attributes, new OperatorAttribute(" and ", " && "));
        this.put(attributes, new OperatorAttribute(" or ", " || "));
        this.put(attributes, new OperatorAttribute("not ", "! "));
        this.put(attributes, new OperatorAttribute(" = ", " == "));
        this.put(attributes, new OperatorAttribute(" equal ", " == "));
        this.put(attributes, new RandomFunctionAttribute());
        this.put(attributes, new StatesFunctionAttribute());
        this.put(attributes, new TemplateAttribute("{contains ([^{}]+), ([^{}]+)}", "(%1$s.indexOf(%2$s) > -1)"));
        this.put(attributes, new TemplateAttribute("{containsWord ([^{}]+), '([\\s\\w]+)'}", "(%1$s.search(/\\b%2$s\\b/) > -1)"));
        this.put(attributes, new TemplateAttribute("{verbose (\\d+)}", "'%1$s'.getOrdinal()"));
        this.put(attributes, new TemplateAttribute("{upper ([^{}]+)}", "%1$s.toUpperCase()"));
        this.put(attributes, new TemplateAttribute("{lower ([^{}]+)}", "%1$s.toLowerCase()"));
        this.put(attributes, new TemplateAttribute("{repeatString ([^{}]+), (\\d)}", "%1$s.repeat(%2$s)"));
        this.put(attributes, new VisitsFunctionAttribute());
        this.put(attributes, new QmlMinutesAttribute());
        this.put(attributes, new QmlSecondsAttribute());
        this.put(attributes, new QmlDayAttribute());
        this.put(attributes, new QmlTimeAttribute());
        this.put(attributes, new QmlServerAttribute());
        this.put(attributes, new QmlVersionAttribute());
        this.put(attributes, new QmlStationAttribute());
        this.put(attributes, new QmlLastStationAttribute());

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
        put(this.attributes, attribute);
    }

    protected void put(Map<String, Attribute> map, Attribute attribute) {
        map.put(attribute.getName(), attribute);
    }

    public Map<String, Attribute> getAttributes() {
        return this.attributes;
    }

    public Map<String, Attribute> getConditionalState() {
        return this.conditionalState;
    }

    public Map<String, Attribute> getPreVisitState() {
        return this.preVisitState;
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = getCleanCopy(attributes);
        this.conditionalState = new HashMap<>();;
        this.preVisitState = new HashMap<>();;
    }

    public void setConditionalState(Map<String, Attribute> conditionalState) {
        this.conditionalState = getCleanCopy(conditionalState);
    }

    public void setPreVisitState(Map<String, Attribute> preVisitState) {
        this.preVisitState = getCleanCopy(preVisitState);
    }

    /**
     * Force Jackson to use this method for attributes getter
     * This ensures internal attributes are not serialised
     *
     * @return
     */
    @JsonProperty("attributes")
    public Map<String, Attribute> getQuestAttributes() {

        return this.attributes.entrySet().stream().filter(es -> !(es.getValue() instanceof InternalAttribute))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    /**
     * Force Jackson to use this method for conditionalState getter
     * This ensures internal attributes are not serialised
     *
     * @return
     */
    @JsonProperty("conditionalState")
    public Map<String, Attribute> getQuestConditionalState() {

        return this.conditionalState.entrySet().stream().filter(es -> !(es.getValue() instanceof InternalAttribute))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    /**
     * Force Jackson to use this method for conditionalState getter
     * This ensures internal attributes are not serialised
     *
     * @return
     */
    @JsonProperty("preVisitState")
    public Map<String, Attribute> getQuestPreVisitState() {

        return this.preVisitState.entrySet().stream().filter(es -> !(es.getValue() instanceof InternalAttribute))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    public String getAttributeValue(String name) {
        return this.attributes.get(name).getValue();
    }

    public Map<String, Attribute> copyAttributes() {
        return new HashedMap(this.attributes == null ? Collections.EMPTY_MAP : this.attributes);
    }

    protected Map<String, Attribute> getCleanCopy(Map<String, Attribute> attributes) {
        Map newAttributes = new HashedMap(attributes == null ? Collections.EMPTY_MAP : attributes);
        cleanAttributes(newAttributes);
        return newAttributes;
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
