package net.maitland.quest.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * Created by David on 07/02/2017.
 */
public class GameTest {

    @Test
    public void testFromCollectionStructure() {

        Game deserGame = null;

        try {
            Game game = new Game(new About("title1", "author1"));
            game.getGameQuest().setIntro("intro1");
            game.setChoiceIndex(1);
            game.setChoiceId("choice1");
            game.getQuestPath().push("start");
            game.getQuestPath().push("station1");
            game.getQuestPath().push("station2");

            List<Attribute> attributes = new ArrayList();
            attributes.add(new NumberAttribute("attribute1", "0"));
            attributes.add(new StringAttribute("attribute2", "abc"));
            attributes.add(new StateAttribute("attribute3", "true"));
            game.updateState(attributes);

            ObjectMapper mapper = new ObjectMapper();
            String gameData = mapper.writeValueAsString(game);

            Map gameDataMap = mapper.readValue(gameData, Map.class);

            deserGame = Game.fromCollectionStructure(gameDataMap);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals("Incorrect title after deserialization", "title1", deserGame.getGameQuest().getTitle());
        assertEquals("Incorrect author after deserialization", "author1", deserGame.getGameQuest().getAuthor());
        assertEquals("Incorrect choiceIndex after deserialization", 1, Math.toIntExact(deserGame.getChoiceIndex()));
        assertEquals("Incorrect choiceId after deserialization", "choice1", deserGame.getChoiceId());
        assertEquals("Incorrect number of station in quest path after deserialization", 3, deserGame.getQuestPath().size());
        assertEquals("Incorrect last station in quest path after deserialization", "station2", deserGame.getQuestPath().pop());
        assertEquals("Incorrect second station in quest path after deserialization", "station1", deserGame.getQuestPath().pop());
        assertEquals("Incorrect first station in quest path after deserialization", "start", deserGame.getQuestPath().pop());
        assertEquals("Incorrect NumberAttribute after deserialization", "0", deserGame.getCurrentState().getAttributeValue("attribute1"));
        assertEquals("Incorrect StringAttribute after deserialization", "abc", deserGame.getCurrentState().getAttributeValue("attribute2"));
        assertEquals("Incorrect StateAttribute after deserialization", "true", deserGame.getCurrentState().getAttributeValue("attribute3"));

    }

    private static String GAME_DATA = "\"gameQuest\": {\n" +
            "        \"title\": \"Chance Dungeon\",\n" +
            "        \"author\": \"David Maitland\",\n" +
            "        \"intro\": \"You are a bold adventurer who in his journeys across the new world have heard of uncharted caves or dungeons that many have been into but not returned from. \\n            You have taken the challenge to map them and have stocked up supplies. \"\n" +
            "      },\n" +
            "      \"questPath\": [\n" +
            "        \"take movement card\",\n" +
            "        \"take equipment\",\n" +
            "        \"check fight status\",\n" +
            "        \"hit monster\",\n" +
            "        \"check fight status\",\n" +
            "        \"hit monster\",\n" +
            "        \"check fight status\",\n" +
            "        \"hit monster\",\n" +
            "        \"fight monster\",\n" +
            "        \"take movement card\",\n" +
            "        \"start\"\n" +
            "      ],\n" +
            "      \"choiceIndex\": 1,\n" +
            "      \"currentState\": {\n" +
            "        \"attributes\": {\n" +
            "          \"[has pole]\": \"false\",\n" +
            "          \"{random 1, 6}\": \"(Math.floor(Math.random() * 6) + 1).toString()\",\n" +
            "          \"{random 1, 1000}\": \"(Math.floor(Math.random() * 1000) + 1).toString()\",\n" +
            "          \"[has lantern]\": \"true\",\n" +
            "          \"[monster hitpoints]\": \"0\",\n" +
            "          \"[has tinderbox]\": \"true\",\n" +
            "          \"[has oil]\": \"false\",\n" +
            "          \" greater \": \"  >\",\n" +
            "          \"{random 0, 100}\": \"(Math.floor(Math.random() * 100) + 1).toString()\",\n" +
            "          \"[torches]\": \"6\",\n" +
            "          \"[has rope]\": \"false\",\n" +
            "          \"[light cards left counter]\": \"8\",\n" +
            "          \"[hitpoints]\": \"5\",\n" +
            "          \"[has backpack]\": \"true\",\n" +
            "          \"[cardNumber]\": \"6\",\n" +
            "          \"not \": \"! \",\n" +
            "          \"[adjustment]\": \"' '\",\n" +
            "          \"{random 1, 13}\": \"(Math.floor(Math.random() * 13) + 1).toString()\",\n" +
            "          \"[lantern duration]\": \"10\",\n" +
            "          \" = \": \" == \",\n" +
            "          \"[gold pieces]\": \"0\",\n" +
            "          \"[change]\": \"0\",\n" +
            "          \"[cards]\": \"2\",\n" +
            "          \"[came from]\": \"'south'\",\n" +
            "          \" or \": \" || \",\n" +
            "          \"[hunger threshold]\": \"6\",\n" +
            "          \"[open paths]\": \"3\",\n" +
            "          \"[torch duration]\": \"5\",\n" +
            "          \" lower \": \" < \",\n" +
            "          \" and \": \" && \",\n" +
            "          \"[has tinder box]\": \"false\",\n" +
            "          \"[meals]\": \"4\",\n" +
            "          \"[hunger cards counter]\": \"2\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"previousState\": {\n" +
            "        \"attributes\": {\n" +
            "          \"[has pole]\": \"false\",\n" +
            "          \"{random 1, 6}\": \"(Math.floor(Math.random() * 6) + 1).toString()\",\n" +
            "          \"{random 1, 1000}\": \"(Math.floor(Math.random() * 1000) + 1).toString()\",\n" +
            "          \"[has lantern]\": \"true\",\n" +
            "          \"[monster hitpoints]\": \"0\",\n" +
            "          \"[has tinderbox]\": \"true\",\n" +
            "          \"[has oil]\": \"false\",\n" +
            "          \" greater \": \"  >\",\n" +
            "          \"{random 0, 100}\": \"(Math.floor(Math.random() * 100) + 1).toString()\",\n" +
            "          \"[torches]\": \"6\",\n" +
            "          \"[has rope]\": \"false\",\n" +
            "          \"[light cards left counter]\": \"8\",\n" +
            "          \"[hitpoints]\": \"5\",\n" +
            "          \"[has backpack]\": \"true\",\n" +
            "          \"[cardNumber]\": \"6\",\n" +
            "          \"not \": \"! \",\n" +
            "          \"[adjustment]\": \"' '\",\n" +
            "          \"{random 1, 13}\": \"(Math.floor(Math.random() * 13) + 1).toString()\",\n" +
            "          \"[lantern duration]\": \"10\",\n" +
            "          \" = \": \" == \",\n" +
            "          \"[gold pieces]\": \"0\",\n" +
            "          \"[change]\": \"0\",\n" +
            "          \"[cards]\": \"2\",\n" +
            "          \"[came from]\": \"'south'\",\n" +
            "          \" or \": \" || \",\n" +
            "          \"[hunger threshold]\": \"6\",\n" +
            "          \"[open paths]\": \"1\",\n" +
            "          \"[torch duration]\": \"5\",\n" +
            "          \" lower \": \" < \",\n" +
            "          \" and \": \" && \",\n" +
            "          \"[has tinder box]\": \"false\",\n" +
            "          \"[meals]\": \"4\",\n" +
            "          \"[hunger cards counter]\": \"2\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";

}
