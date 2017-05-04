package net.maitland.quest.parser.sax;

import net.maitland.quest.model.Quest;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by David on 04/05/2017.
 */
public class SaxQuestParserTest {

    @Test
    public void simpleQuest() throws Exception {

        SaxQuestParser sqp = new SaxQuestParser();
        InputStream is = null;

        try {
            is = this.getClass().getClassLoader().getResourceAsStream("simple-quest.xml");
            Quest q = sqp.parseQuest(is);

            assertEquals("Simple quest intro read incorrectly", "This is the intro", q.getAbout().getIntro());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Test
    public void questTextWithCarriageReturns() throws Exception {

        SaxQuestParser sqp = new SaxQuestParser();
        InputStream is = null;

        try {
            is = this.getClass().getClassLoader().getResourceAsStream("carriage-return-quest.xml");
            Quest q = sqp.parseQuest(is);

            assertEquals("Carriage return quest intro read incorrectly", "This is the intro with carriage returns", q.getAbout().getIntro());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Test
    public void questTextWithFullStops() throws Exception {

        SaxQuestParser sqp = new SaxQuestParser();
        InputStream is = null;

        try {
            is = this.getClass().getClassLoader().getResourceAsStream("full-stop-quest.xml");
            Quest q = sqp.parseQuest(is);

            assertEquals("Full stops quest intro read incorrectly", "This is the intro. with full stops. and stuff. ", q.getAbout().getIntro());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

}