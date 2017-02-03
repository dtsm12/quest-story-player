package net.maitland.quest.model;

/**
 * Created by David on 18/12/2016.
 */
public class StringAttribute extends Attribute {

    public StringAttribute() {
    }

    public StringAttribute(String name) {
        super(name , "' '");
    }

    public StringAttribute(String name, String value) {
        super(name, "'"+value+"'");
    }

    @Override
    public boolean isValidValue(String value) {
        return true;
    }

    @Override
    public void setValue(String value) {
        super.setValue("'"+value+"'");
    }
}
