package net.maitland.quest.player;

import net.maitland.quest.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by David on 29/12/2016.
 */
public class QuestInstanceTest {
    @Test
    public void getNextStationByChoiceNumber() throws Exception {

        QuestStation start = new QuestStation();
        start.setId("start");
        start.setText(new Text("start"));

        QuestStation one = new QuestStation();
        one.setId("one");
        one.setText(new Text("one"));

        QuestStation two = new QuestStation();
        two.setId("two");
        two.setText(new Text("two"));

        Choice choiceOne = new Choice();
        choiceOne.setStation(one);

        Choice choiceTwo = new Choice();
        choiceTwo.setStation(two);

        start.addChoice(choiceOne);
        start.addChoice(choiceTwo);

        Quest quest = new Quest();
        quest.addStation(start);
        quest.addStation(one);
        quest.addStation(two);

        QuestInstance sut = new QuestInstance(quest);

        // get start
        QuestStateStation beginning = sut.getNextStation(0);
        assertEquals("Start station by number incorrect", start.getId(), beginning.getId());

        // get choice
        QuestStateStation choice = sut.getNextStation(1);
        assertEquals("Chosen station by number incorrect", one.getId(), choice.getId());
    }

    @Test
    public void getNextStationBack() throws Exception {

        QuestState questState = new QuestState();
        Quest quest = new Quest();

        QuestStation first = new QuestStation();
        first.setId("first");
        first.setText(new Text("first text"));

        QuestStation second = new QuestStation();
        second.setId("second");
        second.setText(new Text("second text"));

        Choice secondChoice = new Choice();
        secondChoice.setStation(second);
        List<Choice> firstChoices = new ArrayList<>();
        firstChoices.add(secondChoice);
        first.setChoices(firstChoices);

        Choice backChoice = new Choice();
        backChoice.setStation(QuestStation.getBackStation());
        List<Choice> secondChoices = new ArrayList<>();
        secondChoices.add(backChoice);
        second.setChoices(secondChoices);

        List<QuestStation> stations = new ArrayList<>();
        stations.add(first);
        stations.add(second);

        quest.setStations(stations);

        QuestInstance sut = new QuestInstance(quest);

        sut.getNextStation("first");
        sut.getNextStation("second");
        QuestStateStation finalStation = sut.getNextStation(QuestStation.BACK_STATION_ID);

        assertEquals("Back station is incorrect", first.getId(), finalStation.getId());

    }

    @Test
    public void testWherePostAttributePrecludesOnlyChoice() throws Exception
    {
        QuestStation station2 = new QuestStation();
        station2.setId("station2");
        station2.setText(new Text("station2"));

        QuestStation station3 = new QuestStation();
        station3.setId("station3");
        station3.setText(new Text("station3"));

        QuestStation station4 = new QuestStation();
        station4.setId("station4");
        station4.setText(new Text("station4"));

        IfSection is = new IfSection();
        is.setCheck("not [testBoolean] ");
        is.setText(new Text("If text"));

        Choice choice1 = new Choice();
        choice1.setStation(station3);
        is.addChoice(choice1);

        StateAttribute sa = new StateAttribute();
        sa.setName("testBoolean");
        sa.setValue("true");
        is.addAttribute(sa);

        station2.addCondition(is);

        ElseSection es = new ElseSection();
        es.setText(new Text("Else text"));
        station2.setElseCondition(es);

        Choice choice2 = new Choice();
        choice2.setStation(station4);
        es.addChoice(choice2);

        QuestStation station1 = new QuestStation();
        station1.setId("start");
        station1.setText(new Text("start"));
        Choice startChoice = new Choice();
        startChoice.setStation(station2);
        station1.addChoice(startChoice);

        List<QuestStation> stations = new ArrayList<>();
        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        Quest quest = new Quest();
        quest.setStations(stations);

        QuestInstance sut = new QuestInstance(quest);

        QuestStateStation station = sut.getNextStation(null);

        station = sut.getNextStation("station2");

        assertEquals("Correct number of choices is presented", 1, station.getChoices().size());
        assertEquals("Correct choice is presented", "station3", station.getChoices().get(0).getStationId());

        station = sut.getNextStation("station3");

        assertEquals("Presented choice is no longer available",station3.getId(), station.getId());

    }

}