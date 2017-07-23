package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import net.maitland.quest.model.attribute.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 14/07/2017.
 */
public class QuestSectionConditionSet
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @JacksonXmlProperty(localName = "if")
    @JacksonXmlElementWrapper(useWrapping = false)
    protected List<IfSection> conditions = new ArrayList<>();

    @JacksonXmlProperty(localName = "else")
    protected ElseSection elseCondition;

    public List<IfSection> getConditions() {
        return Collections.unmodifiableList(conditions);
    }

    public void setConditions(List<IfSection> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(IfSection ifSection) {
        this.conditions.add(ifSection);
    }

    public ElseSection getElseCondition() {
        return elseCondition;
    }

    public void setElseCondition(ElseSection elseCondition) {
        this.elseCondition = elseCondition;
    }

    public QuestSection getApplicableCondition(Game game, Map<String, Attribute> attributes, ExpressionEvaluator expressionEvaluator)
    {
        QuestSection applicableQuestSection = null;
        if (this.getConditions().size() > 0) {

            for (IfSection i : this.getConditions()) {
                if (expressionEvaluator.check(i, game, attributes)) {
                    applicableQuestSection = i;
                    log.debug("Applicable section has check '{}'", i.getCheck());
                    break;
                }
            }

            if (applicableQuestSection == null) {
                applicableQuestSection = this.getElseCondition();
            }
        }

        return applicableQuestSection;
    }
}
