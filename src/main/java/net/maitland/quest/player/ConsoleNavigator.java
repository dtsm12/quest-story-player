package net.maitland.quest.player;

import net.maitland.quest.model.*;
import net.maitland.quest.model.attribute.Attribute;
import net.maitland.quest.parser.sax.SaxQuestParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 23/07/2017.
 */
public class ConsoleNavigator {

    private static final String NAVIGATE_COMMAND = "go <station id>";
    private static final String GET_COMMAND = "get <attribute name>";
    private static final String SET_COMMAND = "set <attribute name>";
    private static final String LIST_STATIONS_COMMAND = "list stations";
    private static final String LIST_ATRIBUTES_COMMAND = "list attributes";
    private static final String[] COMMANDS = {NAVIGATE_COMMAND, GET_COMMAND, SET_COMMAND, LIST_ATRIBUTES_COMMAND, LIST_STATIONS_COMMAND};

    private static final Pattern GET_PATTERN = Pattern.compile("get (.*)");
    private static final Pattern SET_PATTERN = Pattern.compile("set (.*)");
    private static final Pattern NAVIGATE_PATTERN = Pattern.compile("go (.*)");

    String questFilePath;

    public static void main(String[] args) {
        ConsoleNavigator player = new ConsoleNavigator(args.length > 0 ? args[0] : "alien-quest.xml");
        player.play();
    }

    public ConsoleNavigator(String questFilePath) {
        this.questFilePath = questFilePath;
    }

    public void play() {
        Quest quest = getQuest();
        BufferedReader user = getUsersInput();

        System.out.println(quest.getAbout().getTitle());
        System.out.print(" by ");
        System.out.println(quest.getAbout().getAuthor());
        System.out.println(quest.getAbout().getIntro());

        try {
            Game game = quest.newGameInstance();
            System.out.println("list/set/get/go: ");
            String input = getUsersInput().readLine().toLowerCase();

            while (input.startsWith("exit") == false) {

                if (input.startsWith("set ")) {
                    updateAttribute(game, input);
                } else if (input.startsWith("get ")) {
                    getAttribute(game, input);
                } else if (input.startsWith("list stations")) {
                    listStations(quest);
                } else if (input.startsWith("list attributes")) {
                    listAttributes(game);
                } else if (input.startsWith("go ")) {
                    navigate(quest, game, input);
                } else {
                    System.err.println("Unknown command: '" + input + "'");
                    System.err.println("Valid commands:");
                    for (int i = 0; i < COMMANDS.length; i++) {
                        System.err.println("\t" + COMMANDS[i]);
                    }
                }
                input = getUsersInput().readLine().toLowerCase();
            }

        } catch (QuestStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void updateAttribute(Game game, String input) throws IOException {
        List<String> values = extractValues(SET_PATTERN, input);
        String name = values.get(0);
        System.out.println("Input value: ");
        String value = getUsersInput().readLine().toLowerCase();
        Map<String, Attribute> attributes = game.copyAttributes();
        attributes.get(name).setValue(value);
    }

    protected void getAttribute(Game game, String input) {
        List<String> values = extractValues(GET_PATTERN, input);
        String name = values.get(0);
        Attribute attribute = game.getAttributes().get(name);
        if (attribute == null) {
            System.out.println("Attribute '" + name + "' does not exist.");
        } else {
            String value = attribute.getValue();
            System.out.println(name + ": " + value);
        }
    }

    protected void listAttributes(Game game) {
        System.out.println("Attribute list:");

        for (String name : game.getAttributes().keySet()) {
            System.out.println("\t" + name);
        }
    }

    protected void listStations(Quest quest) {
        System.out.println("Station list:");

        for (QuestStation qs : quest.getStations()) {
            System.out.println("\t" + qs.getId());
        }
    }

    protected void navigate(Quest quest, Game game, String input) {
        List<String> values = extractValues(NAVIGATE_PATTERN, input);
        String stationId = values.get(0);
        QuestStation questStation = quest.getStations().stream().filter(qs -> stationId.equals(qs.getId())).findFirst().orElse(null);
        VisitingStation visitingStation = new VisitingStation(questStation, game);
        GameStation gameStation = visitingStation.visit();

        System.out.println("id: " + gameStation.getId());
        System.out.println("text: " + gameStation.getText());
        System.out.println("choices: ");
        gameStation.getChoices().stream().forEach(c -> System.out.println("\t id: '" + c.getStationId() + "', \t text: '" + c.getText() + "'"));

        visitingStation.leave();
    }

    protected List<String> extractValues(Pattern regEx, String input) {
        List<String> values = new ArrayList<>();

        Matcher m = regEx.matcher(input);
        m.find();

        for (int i = 1; i < m.groupCount() + 1; i++) {
            values.add(m.group(i));
        }

        return values;
    }

    protected BufferedReader getUsersInput() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    protected Quest getQuest() {
        Quest q = null;
        InputStream is = null;

        try {
            is = ConsolePlayer.class.getClassLoader().getResourceAsStream(this.questFilePath);

            if (is == null) {
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
