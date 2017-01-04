package net.maitland.quest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.maitland.quest.jackson.TextDeserializer;
import net.maitland.quest.model.Quest;
import net.maitland.quest.model.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * Created by David on 10/12/2016.
 */
public class QuestParser {

    private XmlMapper xmlMapper;

    public Quest parseQuest(InputStream story) throws IOException {

        String storyStr = removeMixedContent(story);

        return getXmlMapper().readValue(storyStr, Quest.class);
    }

    protected String removeMixedContent(InputStream story) throws IOException {

        StringBuilder cleanedContent = new StringBuilder();

        Scanner scanner = new Scanner(story);
        scanner.useDelimiter("<text");

        while(scanner.hasNext())
        {
            cleanedContent.append(scanner.next());
            scanner.useDelimiter("</text>");
            if(scanner.hasNext()) {
                cleanedContent.append("<text>");
                cleanedContent.append(clean(scanner.next()));
                scanner.useDelimiter("<text");
            }
        }


        return cleanedContent.toString();
    }

    protected String clean(String textContent)
    {
        return textContent.replaceAll("</{0,1}[^>]+/{0,1}>", "");
    }

    public XmlMapper getXmlMapper() {
        if (xmlMapper == null) {
            xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
            /*
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Text.class, new TextDeserializer());
            xmlMapper.registerModule(module);
            */
        }
        return xmlMapper;
    }
}
