
/**
 * Created by David on 04/12/2016.
 */
package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Quest {

    private About about;

    @JacksonXmlProperty(localName = "station")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<QuestStation> stations = new ArrayList<>();

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    public List<QuestStation> getStations() {
        return stations;
    }

    public void setStations(List<QuestStation> stations) {
        this.stations = stations;
    }

    protected void collectAllAttributes(QuestSection questSection, Map<String, String> attributes) {

        if (questSection != null) {
            for (NumberAttribute n : questSection.getNumberAttributes()) {
                attributes.put(n.getName(), "0");
            }

            for (StateAttribute st : questSection.getStateAttributes()) {
                attributes.put(st.getName(), "false");
            }
        }
    }

    protected void collectAllAttributes(QuestStation s, Map<String, String> attributes) {
        collectAllAttributes((QuestSection) s, attributes);
        for (IfSection is : s.getConditions()) {
            collectAllAttributes(is, attributes);
        }
        collectAllAttributes(s.getElseCondition(), attributes);
    }

    public Map<String, String> collectAllAttributes() {

        Map<String, String> attributes = new HashMap<>();

        for(QuestStation qs : this.stations)
        {
            collectAllAttributes(qs, attributes);
        }

        return attributes;
    }
}
