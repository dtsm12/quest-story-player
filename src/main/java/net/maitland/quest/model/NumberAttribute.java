package net.maitland.quest.model;

/**
 * Created by David on 18/12/2016.
 */
public class NumberAttribute extends Attribute {

    public NumberAttribute() {
    }

    public NumberAttribute(String name, int value) {
        super(name, String.valueOf(value));
    }

    @Override
    public boolean isValidValue(String value) {
        boolean isValidValue = false;
        try {
            Integer.parseInt(value);
            isValidValue = true;
        } catch (NumberFormatException nfe) {
        }
        return isValidValue;
    }

}
