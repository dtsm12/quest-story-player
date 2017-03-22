package net.maitland.quest.model.attribute;

/**
 * Created by David on 18/12/2016.
 */
public class StringAttribute extends SimpleAttribute {

    public StringAttribute() {
    }

    public StringAttribute(String name) {
        this(name , "' '");
    }

    public StringAttribute(String name, String value) {
        super(name, value);
    }

    @Override
    protected String getExpressionValue() {
        return surround(super.getValue());
    }

    protected static String surround(String value){

        String prefix = value != null && value.startsWith("'") ? "" : "'";
        String suffix = value != null && value.endsWith("'") ? "" : "'";
        return prefix+value+suffix;
    }
}
