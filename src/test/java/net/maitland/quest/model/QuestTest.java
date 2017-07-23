package net.maitland.quest.model;

import net.maitland.quest.model.attribute.StateAttribute;
import net.maitland.quest.player.ChoiceNotPossibleException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by David on 29/12/2016.
 */
public class QuestTest {
    @Test
    public void getNextStationByChoice() throws Exception {

        QuestStation start = new QuestStation();
        start.setId("start");
        start.addText(new Text("start"));

        QuestStation one = new QuestStation();
        one.setId("one");
        one.addText(new Text("one"));

        QuestStation two = new QuestStation();
        two.setId("two");
        two.addText(new Text("two"));

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
        game.setChoiceId(QuestStation.START_STATION_ID);

        // get start
        GameStation beginning = sut.getNextStation(game);
        assertEquals("Start station by number incorrect", start.getId(), beginning.getId());
        assertNull("Game choice id has not been reset", game.getChoiceId());

        // get choice
        game.setChoiceIndex(1);
        GameStation choice = sut.getNextStation(game);
        assertEquals("Chosen station by number incorrect", one.getId(), choice.getId());
        assertNull("Game choice index has not been reset", game.getChoiceIndex());
    }

    @Test
    public void getNextStationBack() throws Exception {

        Quest sut = new Quest();

        QuestStation first = new QuestStation();
        first.setId("first");
        first.addText(new Text("first text"));

        QuestStation second = new QuestStation();
        second.setId("second");
        second.addText(new Text("second text"));

        QuestStation third = new QuestStation();
        third.setId("third");
        third.addText(new Text("third text"));

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
        station2.addText(new Text("station2"));

        QuestStation station3 = new QuestStation();
        station3.setId("station3");
        station3.addText(new Text("station3"));

        QuestStation station4 = new QuestStation();
        station4.setId("station4");
        station4.addText(new Text("station4"));

        IfSection is = new IfSection();
        is.setCheck("not [testBoolean] ");
        is.addText(new Text("If text"));

        Choice choice1 = new Choice();
        choice1.setStationId(station3.getId());
        is.addChoice(choice1);

        StateAttribute sa = new StateAttribute("testBoolean", "true");
        is.addAttribute(sa);

        station2.addCondition(is);

        ElseSection es = new ElseSection();
        es.addText(new Text("Else text"));
        station2.setElseCondition(es);

        Choice choice2 = new Choice();
        choice2.setStationId(station4.getId());
        es.addChoice(choice2);

        QuestStation station1 = new QuestStation();
        station1.setId("start");
        station1.addText(new Text("start"));
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

    @Test
    public void testEvaluationOrder()
    {
        Quest sut = new Quest();

        // Station with include not applicable after pre-visit attributes
        // - text=includeStation1
        QuestStation includeStation1 = new QuestStation();
        includeStation1.setId("includeStation1");
        IncludedStation is1 = new IncludedStation();
        is1.setCheck("[includeStation1]");
        is1.addStationPatterns("current*");
        includeStation1.setStationIncludeRules(is1);
        includeStation1.addAttribute(new StateAttribute("includeStation1", "false"));
        includeStation1.addText(new Text("includeStation1"));
        includeStation1.addText(new Text("includeStation1Attribute=[includeStation1]"));
        includeStation1.addAttribute(new StateAttribute("postIncludeStation1", "true"));
        sut.addStation(includeStation1);

        // Station with include only applicable after pre-visit attributes
        // - text=includeStation2
        QuestStation includeStation2 = new QuestStation();
        includeStation2.setId("includeStation1");
        IncludedStation is2 = new IncludedStation();
        is2.setCheck("[includeStation2]");
        is2.addStationPatterns("current*");
        includeStation2.setStationIncludeRules(is2);
        includeStation2.addAttribute(new StateAttribute("includeStation2", "true"));
        includeStation2.addText(new Text("includeStation2"));
        includeStation2.addAttribute(new StateAttribute("postIncludeStation2", "true"));
        sut.addStation(includeStation2);

        // Target station where:
        //  - pre-visit attributes affect included stations
        //  - if section not valid after if section pre-visit attributes
        //  - if-section, text=ifSection
        //  - if-section, pre-visit attributes ifSection=false

        QuestStation currentStation =  new QuestStation();
        currentStation.setId("currentStation");
        currentStation.addAttribute(new StateAttribute("ifSection", "true"));
        currentStation.addText(new Text("currentStation"));

        IfSection ifSection = new IfSection();
        ifSection.setCheck("[ifSection]");
        ifSection.addAttribute(new StateAttribute("ifSection", "false"));
        ifSection.addText(new Text("ifSection"));
        ifSection.addText(new Text("ifSectionAttribute=[ifSection]"));
        ifSection.addAttribute(new StateAttribute("postIfSection", "true"));
        currentStation.addCondition(ifSection);

        QuestStation nextStation =  new QuestStation();
        nextStation.setId("nextStation");

        Choice choice = new Choice();
        choice.setStationId("nextStation");
        currentStation.addChoice(choice);

        sut.addStation(currentStation);
        sut.addStation(nextStation);


        // Game State
        //  - includeStation1=true
        //  - includeStation2=false
        //  - ifSection=true
        Game game = sut.newGameInstance();
        game.put(new StateAttribute("includeStation1", "true"));
        game.put(new StateAttribute("includeStation2", "false"));
        game.put(new StateAttribute("ifSection", "true"));
        game.setChoiceId("currentStation");

        try {
            // pre-visit
            GameStation gameStation = sut.getNextStation(game);

            // check correct includes
            //  - text does include "includeStation1"
            //  - text does not include "includeStation2"
            //  - text does include "ifSection"
            assertTrue("Station text from valid include not present", gameStation.getText().contains("includeStation1"));
            assertFalse("Station text from invalid include present", gameStation.getText().contains("includeStation2"));
            assertTrue("Station text from valid if section not present", gameStation.getText().contains("ifSection"));
            assertTrue("Pre-visit attribute from valid include not updated", gameStation.getText().contains("includeStation1Attribute=false"));
            assertFalse("Pre-visit attribute from invalid include updated when it should not be", gameStation.getText().contains("includeStation2"));
            assertTrue("Pre-visit attribute in applicable if section not updated", gameStation.getText().contains("ifSectionAttribute=false"));

            // cause post-visit
            game.setChoiceId("nextStation");
            gameStation = sut.getNextStation(game);

            assertTrue("Post-visit attribute from valid include not updated", new Boolean(game.getAttributeValue("postIncludeStation1")));
            assertFalse("Post-visit attribute from invalid include updated when it should not be", new Boolean(game.getAttributeValue("postIncludeStation2")));
            assertTrue("Post-visit attribute in applicable if section not updated", new Boolean(game.getAttributeValue("postIfSection")));

        } catch (ChoiceNotPossibleException e) {
            fail(e.getMessage());
        }
    }

}