package net.maitland.quest.model.attribute;

import net.maitland.quest.model.QuestState;

import java.util.Arrays;
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
        super("(.*)" + nameRegEx.replace("{", "\\{").replace("}", "\\}") + "(.*)", valueFormat);
        this.namePattern = Pattern.compile(this.getName());
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.namePattern = Pattern.compile(this.getName());
    }

    @Override
    public String replace(String value, QuestState questState) {
        String ret = value;
        Matcher m = namePattern.matcher(value);

        if (m.matches()) {
            int groupCount = m.groupCount()+1; // add one for whole pattern
            Object[] groups = new Object[groupCount];
            for (int i = 1; i < groupCount; i++) {
                groups[i-1] = m.group(i);
            }
            Object[] r = Arrays.copyOfRange(groups, 1, groups.length-2);
            ret = groups[0] + processTemplateValues(r, questState) + groups[groups.length-2];
        }

        return ret;
    }

    protected String processTemplateValues(Object[] r, QuestState questState) {
        return String.format(this.getValue(), r);
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
