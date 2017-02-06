package net.maitland.quest.player;

import net.maitland.quest.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by David on 28/12/2016.
 */
public class QuestStationTest {
    @Test
    public void visitWithAttribute() throws Exception {

        QuestStation sut = new QuestStation();

        sut.setText(new Text("Test [test] text."));
        Map<String, String> attributes = new HashMap<>();
        attributes.put("[test]", "this");
        QuestState questState =  new QuestState(attributes);

        String text = sut.getText(questState).getValue();
        assertEquals("Text containing attribute not substituted correctly", "Test this text.", text);
    }

    @Test
    public void visitWithIfCondition() throws Exception {

        QuestStation sut = new QuestStation();

        sut.setText(new Text("Wrong text"));
        IfSection is = new IfSection();
        is.setCheck("[testValue] == 1");
        is.setText(new Text("Correct text"));
        List<IfSection> conditions = new ArrayList<>();
        conditions.add(is);
        sut.setConditions(conditions);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("[testValue]", "1");
        QuestState questState =  new QuestState(attributes);

        String text = sut.getText(questState).getValue();
        assertEquals("Text from if condition not returned", "Correct text", text);
    }

    @Test
    public void visitWithIfConditionWithMatchingAttribute() throws Exception {

        QuestStation sut = new QuestStation();

        IfSection is = new IfSection();
        is.setCheck("[testCondition] == 3");
        is.setText(new Text("If text"));
        List<IfSection> conditions = new ArrayList<>();
        conditions.add(is);
        sut.setConditions(conditions);

        ElseSection es = new ElseSection();
        es.setText(new Text("Else text"));
        sut.setElseCondition(es);

        NumberAttribute na = new NumberAttribute("testCondition", "3");
        List<NumberAttribute> attributes = new ArrayList<>();
        attributes.add(na);
        sut.setNumberAttributes(attributes);

        List<QuestStation> stations = new ArrayList<>();
        stations.add(sut);
        Quest quest = new Quest();
        quest.setStations(stations);

        Game game = quest.newGameInstance();

        String text = sut.getText(game.getCurrentState()).getValue();
        assertEquals("Text from else condition not returned", "Else text", text);

    }

    @Test
    public void visitAndgetChoicesWithIfConditionWithMatchingAttribute() throws Exception {

        QuestStation sut = new QuestStation();

        IfSection is = new IfSection();
        is.setCheck("[testCondition] == 3");
        is.setText(new Text("If text"));
        List<IfSection> conditions = new ArrayList<>();
        Choice ifChoice = new Choice();
        ifChoice.setText("ifChoice");
        List<Choice> ifChoices = new ArrayList<>();
        ifChoices.add(ifChoice);
        is.setChoices(ifChoices);
        conditions.add(is);
        sut.setConditions(conditions);

        ElseSection es = new ElseSection();
        es.setText(new Text("Else text"));
        Choice elseChoice = new Choice();
        elseChoice.setText("elseChoice");
        List<Choice> elseChoices = new ArrayList<>();
        elseChoices.add(elseChoice);
        es.setChoices(elseChoices);
        sut.setElseCondition(es);

        NumberAttribute na = new NumberAttribute("testCondition", "3");
        List<NumberAttribute> elseAttributes = new ArrayList<>();
        elseAttributes.add(na);
        es.setNumberAttributes(elseAttributes);

        List<QuestStation> stations = new ArrayList<>();
        stations.add(sut);
        Quest quest = new Quest();
        quest.setStations(stations);

        Game game = quest.newGameInstance();

        List<Choice> choices = sut.getChoices(game.getCurrentState());
        Choice choice = choices.get(0);
        assertEquals("Text from else choice not returned", "elseChoice", choice.getText());

    }
}