package net.maitland.quest.model;

/**
 * Created by David on 23/12/2016.
 */
public abstract class Attribute {
    private String name;
    private String value;

    public Attribute() {
    }

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public abstract Attribute updateValue(String newValue);

    public abstract boolean isValidValue(String value);

    public String getType(){
        return this.getClass().getCanonicalName();
    }

    public void setType(String newType)
    {
        throw new UnsupportedOperationException("Type cannot be set");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpressionValue() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String replace(String value)
    {
        return value.replace(getName(), getExpressionValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        return name.equals(attribute.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
