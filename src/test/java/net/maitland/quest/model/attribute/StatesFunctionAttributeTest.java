package net.maitland.quest.model.attribute;

import net.maitland.quest.model.About;
import net.maitland.quest.model.Game;
import net.maitland.quest.model.StateAttribute;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 26/02/2017.
 */
public class StatesFunctionAttributeTest {

    @Test
    public void multipleTrueStates() {

        Game game = new Game(new About("test", "test"));
        game.put(new StateAttribute("hasOne", Boolean.TRUE.toString()));
        game.put(new StateAttribute("hasTwo", Boolean.TRUE.toString()));

        String value = "{states 'has'}";

        StatesFunctionAttribute sut = new StatesFunctionAttribute();

        String replaced = sut.replace(value, game);

        assertEquals("States has wrong value", "hasOne, hasTwo", replaced);

    }

    @Test
    public void trueAndFalseStates() {

        Game game = new Game(new About("test", "test"));
        game.put(new StateAttribute("hasOne", Boolean.TRUE.toString()));
        game.put(new StateAttribute("hasTwo", Boolean.FALSE.toString()));
        game.put(new StateAttribute("hasThree", Boolean.TRUE.toString()));

        String value = "{states 'has'}";

        StatesFunctionAttribute sut = new StatesFunctionAttribute();

        String replaced = sut.replace(value, game);

        assertEquals("States has wrong value", "hasOne, hasThree", replaced);

    }

    @Test
    public void oneTrueStates() {

        Game game = new Game(new About("test", "test"));
        game.put(new StateAttribute("hasOne", Boolean.TRUE.toString()));

        String value = "{states 'has'}";

        StatesFunctionAttribute sut = new StatesFunctionAttribute();

        String replaced = sut.replace(value, game);

        assertEquals("States has wrong value", "hasOne", replaced);

    }

    @Test
    public void oneFalseStates() {

        Game game = new Game(new About("test", "test"));
        game.put(new StateAttribute("hasOne", Boolean.FALSE.toString()));

        String value = "{states 'has'}";

        StatesFunctionAttribute sut = new StatesFunctionAttribute();

        String replaced = sut.replace(value, game);

        assertEquals("States has wrong value", "", replaced);

    }
}
