package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

/**
 * Created by David on 18/12/2016.
 */
public class OperatorAttribute extends Attribute {

    public OperatorAttribute() {
    }

    public OperatorAttribute(String name) {
        super(name , "");
    }

    public OperatorAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public String replace(String value, Game game, boolean isCheck) {
        String ret = value;
        if(isCheck) {
            ret = super.replace(value, game, isCheck);
        }

        return ret;
    }

    @Override
    public boolean isValidValue(String value) {
        return true;
    }
}
