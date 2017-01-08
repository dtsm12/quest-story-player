package net.maitland.quest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by David on 07/01/2017.
 */
public abstract class AbstractQuestParser implements QuestParser {
    protected String removeMixedContent(InputStream story) throws IOException {

        StringBuilder cleanedContent = new StringBuilder();

        Scanner scanner = new Scanner(story);
        scanner.useDelimiter("<text");

        while (scanner.hasNext()) {
            cleanedContent.append(scanner.next());
            scanner.useDelimiter("</text>");
            if (scanner.hasNext()) {
                cleanedContent.append("<text>");
                cleanedContent.append(clean(scanner.next()));
                scanner.useDelimiter("<text");
            }
        }


        return cleanedContent.toString();
    }

    protected String clean(String textContent) {
        return textContent.replaceAll("</{0,1}[^>]+/{0,1}>", "");
    }
}
