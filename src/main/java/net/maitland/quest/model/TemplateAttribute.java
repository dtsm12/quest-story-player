package net.maitland.quest.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 18/02/2017.
 */
public class TemplateAttribute extends Attribute {

    private Pattern namePattern;

    public TemplateAttribute() {
    }

    public TemplateAttribute(String nameRegEx, String valueFormat) {
        super(".*" + nameRegEx.replace("{", "\\{").replace("}", "\\}") + ".*", valueFormat);
        this.namePattern = Pattern.compile(this.getName());
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.namePattern = Pattern.compile(this.getName());
    }

    @Override
    public String replace(String value) {
        String ret = value;
        Matcher m = namePattern.matcher(value);

        if (m.matches()) {
            int groupCount = m.groupCount()+1; // add one for whole pattern
            Object[] groups = new Object[groupCount];
            for (int i = 1; i < groupCount; i++) {
                groups[i-1] = m.group(i);
            }
            ret = String.format(this.getValue(), groups);
        }

        return ret;
    }

    @Override
    public Attribute updateValue(String newValue) {
        return new TemplateAttribute(this.getName(), newValue);
    }

    @Override
    public boolean isValidValue(String value) {
        return true;
    }
}
