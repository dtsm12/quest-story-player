package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;
import net.maitland.quest.model.QuestStateException;

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

    public boolean isValidValue(String value) {
        return true;
    }

    public String getType() {
        return this.getClass().getCanonicalName();
    }

    public void setType(String newType) {
        throw new UnsupportedOperationException("Type cannot be set");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String getExpressionValue() {
        return getValue();
    }

    public String getValue() {
        return value;
    }

    public Attribute updateValue(String newValue) {

        try {
            Attribute a = (Attribute) Class.forName(this.getType()).newInstance();

            a.setName(this.getName());
            a.setValue(newValue);

            return a;

        } catch (Exception e) {
            throw new QuestStateException("Error updating attribute. Value:'" + this.getValue() + "', Type:'" + this.getType() + "'", e);
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String replace(String value, Game game) {
        return replace(value, game, true);
    }

    public String replace(String value, Game game, boolean isCheck) {
        return value.replace(getName(), getValue(isCheck));
    }

    public String getValue(boolean isCheck) {
        return isCheck ? getExpressionValue() : getValue();
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
