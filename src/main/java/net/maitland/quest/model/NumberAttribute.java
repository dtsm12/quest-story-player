package net.maitland.quest.model;

/**
 * Created by David on 18/12/2016.
 */
public class NumberAttribute extends Attribute {

    public NumberAttribute() {
    }

    public NumberAttribute(String name) {
        super(name, "0");
    }

    public NumberAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public Attribute updateValue(String newValue) {
        return new NumberAttribute(this.getName(), toInt(newValue));
    }

    @Override
    public boolean isValidValue(String value) {
        boolean isValidValue = false;
        try {
            Float.parseFloat(value);
            isValidValue = true;
        } catch (NumberFormatException nfe) {
        }
        return isValidValue;
    }

    protected String toInt(String value)
    {
        String ret = value;

        if(isValidValue(value))
        {
            int decimal = value.indexOf(".");
            if(decimal > -1)
            {
                ret = value.substring(0, decimal);
                ret = ret.length() > 0 ? ret : "0";
            }
        }

        return ret;
    }

}
