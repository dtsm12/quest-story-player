package net.maitland.quest.player;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import net.maitland.quest.model.QuestStation;

/**
 * Created by David on 13/01/2017.
 */
public class QuestStateChoice {

    private String stationId;

    private String text;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
