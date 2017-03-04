package net.maitland.quest.model.attribute;

/**
 * Created by David on 18/12/2016.
 */
public class StringAttribute extends Attribute {

    public StringAttribute() {
    }

    public StringAttribute(String name) {
        super(name , "' '");
    }

    public StringAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    public String getExpressionValue() {
        return surround(super.getValue());
    }

    protected static String surround(String value){

        String prefix = value != null && value.startsWith("'") ? "" : "'";
        String suffix = value != null && value.endsWith("'") ? "" : "'";
        return prefix+value+suffix;
    }
}
