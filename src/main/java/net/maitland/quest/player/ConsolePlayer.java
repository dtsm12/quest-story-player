package net.maitland.quest.player;

import net.maitland.quest.model.Quest;
import net.maitland.quest.model.*;
import net.maitland.quest.parser.sax.SaxQuestParser;

import java.io.*;
import java.util.List;

/**
 * Created by David on 19/12/2016.
 */
public class ConsolePlayer {

    String questFilePath;

    public static void main(String[] args) {
        ConsolePlayer player = new ConsolePlayer(args.length > 0 ? args[0] : "alien-quest.xml");
        player.play();
    }

    public ConsolePlayer(String questFilePath) {
        this.questFilePath = questFilePath;
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
            is = ConsolePlayer.class.getClassLoader().getResourceAsStream(this.questFilePath);

            if(is == null)
            {
                throw new FileNotFoundException("Unable to find file '" + this.questFilePath + "' on classpath");
            }

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
