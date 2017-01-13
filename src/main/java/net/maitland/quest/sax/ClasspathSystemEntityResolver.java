package net.maitland.quest.sax;

import net.maitland.quest.SaxQuestParser;
import net.maitland.quest.player.ConsolePlayer;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

/**
 * Created by David on 10/01/2017.
 */
public class ClasspathSystemEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        InputStream is = null;

        int lastPath = systemId.lastIndexOf("/");
        String fileName = systemId.substring(lastPath + 1);
        is = this.getClass().getClassLoader().getResourceAsStream(fileName);

        return new InputSource(is);
    }
}
