package net.maitland.quest.player;

import net.maitland.quest.model.Quest;
import net.maitland.quest.model.*;
import net.maitland.quest.parser.sax.SaxQuestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by David on 19/12/2016.
 */
public class ConsolePlayer {

    public static void main(String[] args) {
        ConsolePlayer player = new ConsolePlayer();
        player.play();
    }

    public void play() {
        Quest quest = getQuest();
        BufferedReader user = getUsersInput();

        String choiceId = QuestStation.START_STATION_ID;
        List<GameChoice> choices = null;
        GameStation questStation;

        System.out.println(quest.getAbout().getTitle());
        System.out.print(" by ");
        System.out.println(quest.getAbout().getAuthor());
        System.out.println(quest.getAbout().getIntro());

        try {
            Game game = quest.newGameInstance();

            while (QuestStation.START_STATION_ID.equals(choiceId) || choices != null) {

                try {
                    game.setChoiceId(choiceId);
                    questStation = quest.getNextStation(game);
                    System.out.println(questStation.getText());
                    choices = questStation.getChoices();
                } catch (ChoiceNotPossibleException e) {
                    System.out.println(e.getMessage());

                    if (choices.size() == 1) {
                        // something has gone wrong since single choice was automatically chosen
                        break;
                    }
                }

                if (choices.size() == 0) {
                    break;
                }

                System.out.println();

                if (choices.size() == 1) {
                    GameChoice c = choices.get(0);
                    System.out.println(c.getText());
                    choiceId = c.getStationId();
                } else {
                    System.out.println("These are your choices:");

                    for (GameChoice c : choices) {
                        System.out.println(String.format("\t%s: %s", c.getStationId(), c.getText()));
                    }

                    System.out.println();
                    System.out.println("Enter your choice:");

                    choiceId = getUsersInput().readLine();
                }
            }
        } catch (QuestStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected BufferedReader getUsersInput() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    protected Quest getQuest() {
        Quest q = null;
        InputStream is = null;

        try {
            is = ConsolePlayer.class.getClassLoader().getResourceAsStream("chance-quest.xml");
            SaxQuestParser qp = new SaxQuestParser();
            q = qp.parseQuest(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return q;
    }
}
