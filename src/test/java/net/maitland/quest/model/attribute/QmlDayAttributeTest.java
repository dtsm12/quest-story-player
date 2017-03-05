package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by David on 28/02/2017.
 */
public class QmlDayAttributeTest {

    @Test
    public void it_will_replace_with_the_full_name_of_weekday()
    {
        List daysOfWeek = new ArrayList<>();
        daysOfWeek.add("Monday");
        daysOfWeek.add("Tuesday");
        daysOfWeek.add("Wednesday");
        daysOfWeek.add("Thursday");
        daysOfWeek.add("Friday");
        daysOfWeek.add("Saturday");
        daysOfWeek.add("Sunday");

        QmlDayAttribute sut = new QmlDayAttribute();

        String result = sut.replace("[qmlDay]", new Game(new About("test", "test")), false);

        assertTrue("qmlDay didn't resolve to the name of a day: " + result, daysOfWeek.contains(result));
    }
}
