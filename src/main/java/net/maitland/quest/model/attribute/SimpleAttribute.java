package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

/**
 * Created by David on 05/03/2017.
 */
public class SimpleAttribute extends Attribute {

    public SimpleAttribute() {
    }


    public SimpleAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public String replace(String value, Game game, boolean isCheck) {
        return value.replace("[" + getName() +"]", getValue(isCheck));
    }

    protected static String surroundWithBrackets(String value){

        String prefix = value != null && value.startsWith("[") ? "" : "[";
        String suffix = value != null && value.endsWith("]") ? "" : "]";
        return prefix+value+suffix;
    }
}
