package net.maitland.quest.model;

/**
 * Created by David on 18/12/2016.
 */
public class StateAttribute extends Attribute {

    public StateAttribute() {
    }

    public StateAttribute(String name) {
        super(name, "false");
    }

    public StateAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public Attribute updateValue(String newValue) {
        return new StateAttribute(this.getName(), newValue);
    }

    @Override
    public String getExpressionValue() {
        return getValue();
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
