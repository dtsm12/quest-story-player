package net.maitland.quest.model.attribute;

import net.maitland.quest.model.QuestState;
import net.maitland.quest.model.StateAttribute;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 26/02/2017.
 */
public class StatesFunctionAttributeTest {

    @Test
    public void multipleTrueStates() {

        QuestState state = new QuestState();
        state.put(new StateAttribute("hasOne", Boolean.TRUE.toString()));
        state.put(new StateAttribute("hasTwo", Boolean.TRUE.toString()));

        String value = "{states 'has'}";

        StatesFunctionAttribute sut = new StatesFunctionAttribute();

        String replaced = sut.replace(value, state);

        assertEquals("States has wrong value", "hasOne, hasTwo", replaced);

    }

    @Test
    public void trueAndFalseStates() {

        QuestState state = new QuestState();
        state.put(new StateAttribute("hasOne", Boolean.TRUE.toString()));
        state.put(new StateAttribute("hasTwo", Boolean.FALSE.toString()));
        state.put(new StateAttribute("hasThree", Boolean.TRUE.toString()));

        String value = "{states 'has'}";

        StatesFunctionAttribute sut = new StatesFunctionAttribute();

        String replaced = sut.replace(value, state);

        assertEquals("States has wrong value", "hasOne, hasThree", replaced);

    }

    @Test
    public void oneTrueStates() {

        QuestState state = new QuestState();
        state.put(new StateAttribute("hasOne", Boolean.TRUE.toString()));

        String value = "{states 'has'}";

        StatesFunctionAttribute sut = new StatesFunctionAttribute();

        String replaced = sut.replace(value, state);

        assertEquals("States has wrong value", "hasOne", replaced);

    }

    @Test
    public void oneFalseStates() {

        QuestState state = new QuestState();
        state.put(new StateAttribute("hasOne", Boolean.FALSE.toString()));

        String value = "{states 'has'}";

        StatesFunctionAttribute sut = new StatesFunctionAttribute();

        String replaced = sut.replace(value, state);

        assertEquals("States has wrong value", "", replaced);

    }
}
