
/**
 * Created by David on 04/12/2016.
 */
package net.maitland.quest.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import net.maitland.quest.player.QuestStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Quest {

    private About about;

    @JacksonXmlProperty(localName = "station")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<QuestStation> stations = new ArrayList<>();

    public GameInstance newGameInstance() throws QuestStateException
    {
        GameInstance gameInstance = new GameInstance();
        gameInstance.updateState(this.collectAllAttributes());
        return gameInstance;
    }

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

    public void addStation(QuestStation questStation) {
        this.stations.add(questStation);
    }

    protected void collectAllAttributes(QuestSection questSection, List<Attribute> attributes) {

        if (questSection != null) {
            for (NumberAttribute n : questSection.getNumberAttributes()) {
                attributes.add(new NumberAttribute(n.getName(), 0));
            }

            for (StateAttribute st : questSection.getStateAttributes()) {
                attributes.add(new StateAttribute(st.getName(), false));
            }
        }
    }

    protected void collectAllAttributes(QuestStation s, List<Attribute> attributes) {
        collectAllAttributes((QuestSection) s, attributes);
        for (IfSection is : s.getConditions()) {
            collectAllAttributes(is, attributes);
        }
        collectAllAttributes(s.getElseCondition(), attributes);
    }

    public List<Attribute> collectAllAttributes() {

        List<Attribute> attributes = new ArrayList<>();

        for (QuestStation qs : this.stations) {
            collectAllAttributes(qs, attributes);
        }

        return attributes;
    }
}
