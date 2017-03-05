package net.maitland.quest.model.attribute;

/**
 * Created by David on 05/03/2017.
 */
public class RandomFunctionAttribute extends TemplateAttribute {

    public RandomFunctionAttribute() {
        super("{random (\\d+), (\\d+)}", "(Math.floor(Math.random() * %2$s) + %1$s).toString()");
    }
}
