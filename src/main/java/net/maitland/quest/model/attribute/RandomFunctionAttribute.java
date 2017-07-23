package net.maitland.quest.model.attribute;

/**
 * Created by David on 05/03/2017.
 */
public class RandomFunctionAttribute extends TemplateAttribute {

    public static String value = "(Math.floor(Math.random() * %2$s) + %1$s).toString()";

    public RandomFunctionAttribute() {
        super("{random (\\d+), (\\d+)}", RandomFunctionAttribute.value);
    }
}
