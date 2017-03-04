package net.maitland.quest.model.attribute;

/**
 * Created by David on 04/03/2017.
 */
public class ReadOnlyAttribute extends Attribute {

    public ReadOnlyAttribute() {
    }

    public ReadOnlyAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public void setValue(String value) {
        return;
    }

    @Override
    public Attribute updateValue(String newValue) {
        return this;
    }

}
