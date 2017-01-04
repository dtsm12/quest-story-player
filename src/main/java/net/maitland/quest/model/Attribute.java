package net.maitland.quest.model;

/**
 * Created by David on 23/12/2016.
 */
public abstract class Attribute {
    private String name;
    private String value;

    public abstract boolean isValidValue(String value);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
