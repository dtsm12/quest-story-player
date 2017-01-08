package net.maitland.quest.player;

import net.maitland.quest.JacksonQuestParser;
import net.maitland.quest.SaxQuestParser;
import net.maitland.quest.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
        QuestInstance game = new QuestInstance(quest);
        BufferedReader user = getUsersInput();

        String choiceId = null;
        List<Choice> choices = null;
        QuestStateStation questStation;

        try {
            while (choiceId == null || choices != null) {

                try {
                    questStation = game.getNextStation(choiceId);
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
                    Choice c = choices.get(0);
                    System.out.println(c.getText());
                    choiceId = c.getStation().getId();
                } else {
                    System.out.println("These are your choices:");

                    for (Choice c : choices) {
                        System.out.println(String.format("\t%s: %s", c.getStation().getId(), c.getText()));
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
            is = ConsolePlayer.class.getClassLoader().getResourceAsStream("bargames-quest.xml");
            SaxQuestParser qp = new SaxQuestParser();
            q = qp.parseQuest(is);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
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
