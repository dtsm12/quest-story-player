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

}