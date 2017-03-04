package net.maitland.quest.model;

import net.maitland.quest.model.attribute.StateAttribute;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by David on 29/12/2016.
 */
public class QuestTest {
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
        choiceOne.setStationId(one.getId());
        choiceOne.setText("choiceOne");

        Choice choiceTwo = new Choice();
        choiceTwo.setStationId(two.getId());
        choiceTwo.setText("choiceTwo");

        start.addChoice(choiceOne);
        start.addChoice(choiceTwo);

        Quest sut = new Quest();
        sut.addStation(start);
        sut.addStation(one);
        sut.addStation(two);

        Game game = sut.newGameInstance();

        // get start
        GameStation beginning = sut.getNextStation(game, 0);
        assertEquals("Start station by number incorrect", start.getId(), beginning.getId());

        // get choice
        GameStation choice = sut.getNextStation(game, 1);
        assertEquals("Chosen station by number incorrect", one.getId(), choice.getId());
    }

    @Test
    public void getNextStationBack() throws Exception {

        Quest sut = new Quest();

        QuestStation first = new QuestStation();
        first.setId("first");
        first.setText(new Text("first text"));

        QuestStation second = new QuestStation();
        second.setId("second");
        second.setText(new Text("second text"));

        QuestStation third = new QuestStation();
        third.setId("third");
        third.setText(new Text("third text"));

        Choice secondChoice = new Choice();
        secondChoice.setStationId(second.getId());

        Choice thirdChoice = new Choice();
        thirdChoice.setStationId(third.getId());

        List<Choice> firstChoices = new ArrayList<>();
        firstChoices.add(secondChoice);
        firstChoices.add(thirdChoice);
        first.setChoices(firstChoices);

        Choice backChoice = new Choice();
        backChoice.setStationId(QuestStation.BACK_STATION_ID);
        List<Choice> secondChoices = new ArrayList<>();
        secondChoices.add(backChoice);
        second.setChoices(secondChoices);

        sut.addStation(first);
        sut.addStation(second);

        Game game = sut.newGameInstance();

        sut.getNextStation(game, "first");
        sut.getNextStation(game, "second");
        GameStation finalStation = sut.getNextStation(game, QuestStation.BACK_STATION_ID);

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
        choice1.setStationId(station3.getId());
        is.addChoice(choice1);

        StateAttribute sa = new StateAttribute("testBoolean", "true");
        is.addAttribute(sa);

        station2.addCondition(is);

        ElseSection es = new ElseSection();
        es.setText(new Text("Else text"));
        station2.setElseCondition(es);

        Choice choice2 = new Choice();
        choice2.setStationId(station4.getId());
        es.addChoice(choice2);

        QuestStation station1 = new QuestStation();
        station1.setId("start");
        station1.setText(new Text("start"));
        Choice startChoice = new Choice();
        startChoice.setStationId(station2.getId());
        station1.addChoice(startChoice);

        Quest sut = new Quest();

        sut.addStation(station1);
        sut.addStation(station2);
        sut.addStation(station3);

        Game game = sut.newGameInstance();

        GameStation station = sut.getNextStation(game, null);

        station = sut.getNextStation(game, "station2");

        assertEquals("Correct number of choices is presented", 1, station.getChoices().size());
        assertEquals("Correct choice is presented", "station3", station.getChoices().get(0).getStationId());

        station = sut.getNextStation(game, "station3");

        assertEquals("Presented choice is no longer available",station3.getId(), station.getId());

    }

}