package net.maitland.quest.model;

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
    public Attribute updateValue(String newValue) {
        return new OperatorAttribute(this.getName(), newValue);
    }

    @Override
    public String replace(String value) {
        return value.replace(getName(), getExpressionValue());
    }

    @Override
    public boolean isValidValue(String value) {
        return true;
    }
}
