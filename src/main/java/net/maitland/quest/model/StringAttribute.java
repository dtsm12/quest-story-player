package net.maitland.quest.model;

/**
 * Created by David on 18/12/2016.
 */
public class StringAttribute extends Attribute {

    @Override
    public boolean isValidValue(String value) {
        return true;
    }
}
