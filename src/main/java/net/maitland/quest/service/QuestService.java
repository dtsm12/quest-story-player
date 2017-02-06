package net.maitland.quest.service;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Quest;

import java.util.Set;

/**
 * Created by David on 04/02/2017.
 */
public interface QuestService {

    public Quest getQuest(String questName, String authorName);

    public Set<About> getPopularQuests(int topN);
}
