package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

/**
 * Created by David on 18/12/2016.
 */
public class Text implements Conditional {

    @JacksonXmlText
    public String value;

    public String check;

    public Text() {
    }

    public Text(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
