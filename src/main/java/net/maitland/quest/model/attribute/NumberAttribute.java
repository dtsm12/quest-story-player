package net.maitland.quest.model.attribute;

/**
 * Created by David on 18/12/2016.
 */
public class NumberAttribute extends SimpleAttribute {

    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public NumberAttribute() {
    }

    public NumberAttribute(String name) {
        this(name, "0");
    }

    public NumberAttribute(String name, String value) {
        super(name, value);
    }

    public NumberAttribute(String name, String value, String min, String max) {
        super(name, value);
        setMin(min);
        setMax(max);
    }

    public String getMin() {
        return String.valueOf(min);
    }

    public void setMin(String min) {
        this.min = Integer.parseInt(min);
    }

    public String getMax() {
        return String.valueOf(max);
    }

    public void setMax(String max) {
        this.max = Integer.parseInt(max);
    }

    @Override
    public Attribute updateValue(String newValue) {
        int newInt = Integer.parseInt(toInt(newValue));
        newInt = Math.min(this.max, newInt);
        newInt = Math.max(this.min, newInt);
        NumberAttribute updatedValue = (NumberAttribute) super.updateValue(String.valueOf(newInt));
        updatedValue.setMin(this.getMin());
        updatedValue.setMax(this.getMax());
        return updatedValue;
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

    protected String toInt(String value) {
        String ret = value;

        if (isValidValue(value)) {
            int decimal = value.indexOf(".");
            if (decimal > -1) {
                ret = value.substring(0, decimal);
                ret = ret.length() > 0 ? ret : "0";
            }
        }

        return ret;
    }

}
