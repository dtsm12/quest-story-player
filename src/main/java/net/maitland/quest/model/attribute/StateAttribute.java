package net.maitland.quest.model.attribute;

/**
 * Created by David on 18/12/2016.
 */
public class StateAttribute extends SimpleAttribute {

    public StateAttribute() {
    }

    public StateAttribute(String name) {
        this(name, "false");
    }

    public StateAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public String getValue() {
        return super.getValue() == null ? Boolean.TRUE.toString() : super.getValue();
    }

    @Override
    public boolean isValidValue(String value) {

        return ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value));

    }

    public boolean isTrue()
    {
        return "true".equalsIgnoreCase(getValue());
    }

}
