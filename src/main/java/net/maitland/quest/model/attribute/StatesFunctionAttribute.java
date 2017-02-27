package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;
import net.maitland.quest.model.StateAttribute;

import java.util.stream.Collectors;

/**
 * Created by David on 26/02/2017.
 */
public class StatesFunctionAttribute extends TemplateAttribute {

    public StatesFunctionAttribute() {
        super("{states '*([^{'}]+)'*}", "%1$s");
    }

    @Override
    protected String processTemplateValues(Object[] values, Game game) {
        String statePrefix = (String) values[0];

        String applicableStates = game.getAttributes().values().stream().filter(s -> s instanceof StateAttribute && s.getName().startsWith(statePrefix) && ((StateAttribute) s).isTrue()).map(s -> s.getName()).sorted().collect(Collectors.joining(", "));

        return applicableStates;
    }
}
