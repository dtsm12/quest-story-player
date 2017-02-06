package net.maitland.quest.service;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Quest;
import net.maitland.quest.parser.sax.SaxQuestParser;
import net.maitland.quest.player.ConsolePlayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by David on 04/02/2017.
 */
public class ClasspathResourceQuestService implements QuestService {

    private static Map<About, String> quests = new HashMap<>();

    static {
        About quest1 = new About("Bargames", "Philipp Lenssen");
        quests.put(quest1, "bargames-quest.xml");

        About quest2 = new About("Chance Dungeon", "David Maitland");
        quests.put(quest2, "chance-quest.xml");
    }

    public Quest getQuest(String questName, String authorName) {

        String fileName = quests.get(new About(questName, authorName));
        return getQuest(fileName);
    }

    public Set<About> getPopularQuests(int topN) {
        return quests.keySet().stream().limit(topN).collect(Collectors.toSet());
    }

    protected Quest getQuest(String fileName) {
        Quest q = null;
        InputStream is = null;

        try {
            is = ConsolePlayer.class.getClassLoader().getResourceAsStream(fileName);
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
