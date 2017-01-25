package net.maitland.quest.model;

/**
 * Created by David on 18/12/2016.
 */
public class StateAttribute extends Attribute {

    public StateAttribute() {
    }

    public StateAttribute(String name, boolean value) {
        super(name, String.valueOf(value));
    }

    @Override
    public String getValue() {
        return super.getValue() == null ? Boolean.TRUE.toString() : super.getValue();
    }

    @Override
    public boolean isValidValue(String value) {

        return ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value));

    }

}
