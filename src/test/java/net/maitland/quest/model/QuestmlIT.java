package net.maitland.quest.model;

import net.maitland.quest.model.attribute.Attribute;
import net.maitland.quest.parser.sax.SaxQuestParser;
import net.maitland.quest.player.ConsolePlayer;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.*;

/**
 * Created by David on 03/02/2017.
 */
public class QuestmlIT {


    @Test
    public void test_all_attribute_types() {

        try {
            Quest sut = getQuest();
            Game game = sut.newGameInstance();

            // start quest
            GameStation station = sut.getNextStation(game, 0);

            // move to next station
            station = sut.getNextStation(game, 2);
            List<GameChoice> choices = station.getChoices();
            VisitingStation visitingStation = new VisitingStation(sut.getStation(station.getId()), game);
            Map<String, Attribute> attributes = visitingStation.getConditionalState();

            // test attribute types
            while ("startIncludeTests".equals(station.getId()) == false){

                String stationId = station.getId();
                String attributeValue = attributes.get(stationId).getValue();

                assertEquals(stationId + " does not have 2 choices.", 2, choices.size());
                assertEquals(stationId + " has the wrong text.", "test "+attributeValue, station.getText());

                GameChoice choice1 = choices.get(0);
                assertEquals(stationId + " choice 1 has the wrong text.", "test "+attributeValue, choice1.getText());
                assertEquals(stationId + " choice 1 has the wrong stationId.", "test"+attributeValue, choice1.getStationId());

                station = sut.getNextStation(game, 2);
                choices = station.getChoices();
                visitingStation = new VisitingStation(sut.getStation(station.getId()), game);
                attributes = visitingStation.getConditionalState();
            }

            // move onto include tests
            station = sut.getNextStation(game, 1);
            visitingStation = new VisitingStation(sut.getStation(station.getId()), game);
            attributes = visitingStation.getConditionalState();

            while ("end".equals(station.getId()) == false){

                String stationId = station.getId();
                choices = station.getChoices();

                assertEquals(stationId + " does not have 2 choices.", 2, choices.size());
                assertTrue(stationId + " doesn't start correctly.", station.getText().startsWith(stationId));
                assertTrue(stationId + " doesn't start correctly.", station.getText().endsWith(stationId+"Text"));

                GameChoice choice1 = choices.get(0);
                assertEquals(stationId + " choice 1 has the wrong stationId.", stationId, choice1.getStationId());

                station = sut.getNextStation(game, 2);
                visitingStation = new VisitingStation(sut.getStation(station.getId()), game);
                attributes = visitingStation.getConditionalState();

                String attr = attributes.get(stationId+"State").getValue();
                attr.equals(Boolean.TRUE.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    protected Quest getQuest() throws Exception {
        Quest q = null;
        InputStream is = null;

        try {
            is = ConsolePlayer.class.getClassLoader().getResourceAsStream("questml-test.xml");
            SaxQuestParser qp = new SaxQuestParser();
            q = qp.parseQuest(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return q;
    }
}
