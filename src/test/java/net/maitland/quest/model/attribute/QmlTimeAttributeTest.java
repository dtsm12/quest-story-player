package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by David on 28/02/2017.
 */
public class QmlTimeAttributeTest {

    @Test
    public void it_will_replace_with_the_time()
    {
        QmlTimeAttribute sut = new QmlTimeAttribute();

        String result = sut.replace("[qmlTime]", new Game(new About("test", "test")), false);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.parse(result);
        } catch (Exception e) {
            fail("qmlTime didn't resolve to the time: " + result);
        }
    }
}
