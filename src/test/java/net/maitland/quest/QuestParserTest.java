package net.maitland.quest;

import net.maitland.quest.model.Quest;
import net.maitland.quest.parser.jackson.JacksonQuestParser;
import net.maitland.quest.parser.QuestParser;
import net.maitland.quest.parser.sax.SaxQuestParser;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by David on 18/12/2016.
 */
public class QuestParserTest {

    public void testJacksonParser() throws Exception {
        testParser(new JacksonQuestParser());
    }

    @Test
    public void testSaxParser() throws Exception {
        testParser(new SaxQuestParser());
    }

    protected void testParser(QuestParser questParser) throws Exception{

        InputStream is = null;

        try {
            is = this.getClass().getClassLoader().getResourceAsStream("bargames-quest.xml");
            Quest q = questParser.parseQuest(is);

            assertEquals("Quest title is incorrect", q.getAbout().getTitle(), "Bargames");
            assertEquals("Incorrect number of stations", q.getStations().size(), 29+1); /* 29 + back station */
            assertNotNull("1st station doesn't have text", q.getStations().get(1).getTexts().get(0).getValue());
            assertEquals("1st station doesn't have choice", q.getStations().get(1).getChoices().size(), 1);
            assertNotNull("1st station's choice doesn't have text", q.getStations().get(1).getChoices().get(0).getText());
            assertEquals("4th station doesn't have a number attribute", q.getStations().get(4).getNumberAttributes().size(), 1);
            assertEquals("4th station's number attribute is incorrect", q.getStations().get(4).getNumberAttributes().get(0).getName(), "gold");
            assertEquals("5th station doesn't have an if condition", q.getStations().get(5).getConditions().size(), 1);
            assertEquals("5th station's if condition check is incorrect", q.getStations().get(5).getConditions().get(0).getCheck(), "[gold] greater 0");
            assertEquals("5th station's if condition choice is incorrect", q.getStations().get(5).getConditions().get(0).getChoices().get(0).getStationId(), "bar");
            assertNotNull("5th station doesn't have an else condition", q.getStations().get(5).getElseCondition());
            assertEquals("4th station's if condition check is incorrect", q.getStations().get(5).getElseCondition().getNumberAttributes().get(0).getName(), "gold");
            assertEquals("9th station doesn't have a number attribute", q.getStations().get(9).getStringAttributes().size(), 1);
            assertEquals("9th station's string attribute is incorrect", q.getStations().get(9).getStringAttributes().get(0).getName(), "introDisplay");
            assertEquals("12th station's 2nd choice is not back", q.getStations().get(12).getChoices().get(1).getStationId(), "back");
            assertEquals("16th station doesn't have a state", q.getStations().get(16).getStateAttributes().size(), 1);
            assertEquals("16th station's state is incorrect", q.getStations().get(16).getStateAttributes().get(0).getName(), "ate peanuts");

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}