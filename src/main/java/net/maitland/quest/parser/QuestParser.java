package net.maitland.quest.parser;

import net.maitland.quest.model.Quest;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by David on 06/01/2017.
 */
public interface QuestParser {
    Quest parseQuest(InputStream story) throws IOException;
}
