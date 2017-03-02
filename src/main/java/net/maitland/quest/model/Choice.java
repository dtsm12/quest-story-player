package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

/**
 * Created by David on 18/12/2016.
 */
public class Choice {

    private String stationId;

    private QuestStation station;

    @JacksonXmlText
    private String text = "";

    private String check;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
