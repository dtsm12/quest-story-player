package net.maitland.quest.model.attribute;

/**
 * Created by David on 05/03/2017.
 */
public abstract class GameStateStringAttribute extends GameStateAttribute {

    public GameStateStringAttribute(String name) {
        super(name);
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
