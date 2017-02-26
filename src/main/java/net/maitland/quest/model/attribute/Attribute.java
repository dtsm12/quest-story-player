package net.maitland.quest.model.attribute;

import net.maitland.quest.model.QuestState;
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

    public Attribute updateValue(String newValue) {

        try {
            Attribute a = (Attribute) Class.forName(this.getType()).newInstance();

            a.setName(this.getName());
            a.setValue(this.getValue());

            return a;

        } catch (Exception e) {
           throw new QuestStateException("Error updating attribute. Value:'" + this.getValue() + "', Type:'" + this.getType() + "'", e);
        }
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

    public String getExpressionValue() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String replace(String value, QuestState questState) {
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
