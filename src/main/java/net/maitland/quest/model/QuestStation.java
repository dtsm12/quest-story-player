package net.maitland.quest.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.maitland.quest.parser.jackson.BackStationAwareObjectIdResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 18/12/2016.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        resolver = BackStationAwareObjectIdResolver.class)
public class QuestStation extends QuestSection {

    public static final String START_STATION_ID = "start";
    public static final String BACK_STATION_ID = "back";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static QuestStation backStation;

    private String id;
    private IncludedStation stationIncludeRules;
    private List<IncludedStation> includedStations = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IncludedStation getStationIncludeRules() {
        return stationIncludeRules;
    }

    public void setStationIncludeRules(IncludedStation includedStation) {
        includedStation.setStation(this);
        this.stationIncludeRules = includedStation;
    }

    public List<IncludedStation> getIncludedStations() {
        return includedStations;
    }

    public void addIncludedStation(IncludedStation includedStation) {
        this.includedStations.add(includedStation);
    }

    public static QuestStation getBackStation() {
        if (backStation == null) {
            backStation = new QuestStation();
            backStation.setId(BACK_STATION_ID);
        }
        return backStation;
    }

    public boolean includeIn(String stationId) {

        boolean include = this.stationIncludeRules != null && this.stationIncludeRules.includeIn(stationId);
        return include;
    }
}

