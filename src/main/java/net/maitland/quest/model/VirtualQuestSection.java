package net.maitland.quest.model;

import net.maitland.quest.model.Choice;
import net.maitland.quest.model.Text;
import net.maitland.quest.model.attribute.Attribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 16/03/2017.
 */
public class VirtualQuestSection {

    private List<Text> texts = new ArrayList<>();
    private List<Attribute> preVisitAttributes = new ArrayList<>();
    private List<Attribute> postVisitAttributes = new ArrayList<>();
    private List<Choice> choices = new ArrayList<>();
    private boolean isExclusive = false;

    public List<Text> getTexts() {
        return this.texts;
    }

    public List<Attribute> getPreVisitAttributes() {
        return this.preVisitAttributes;
    }

    public List<Attribute> getPostVisitAttributes() {
        return this.postVisitAttributes;
    }

    public List<Choice> getChoices() {
        return this.choices;
    }

    public void add(QuestSection qs, IncludeProcess insertLocation) {

        if (this.isExclusive == false) {
            addToList(this.texts, qs.getTexts(), insertLocation);
            addToList(this.preVisitAttributes, qs.getPreVisitAttributes(), insertLocation);
            addToList(this.postVisitAttributes, qs.getPostVisitAttributes(), insertLocation);
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
}
