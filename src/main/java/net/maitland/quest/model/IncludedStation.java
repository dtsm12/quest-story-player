package net.maitland.quest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by David on 13/03/2017.
 */
public class IncludedStation implements Conditional {

    private QuestStation station;
    private String check = Boolean.TRUE.toString();
    private IncludeProcess process;
    private List<String> stationPatterns = new ArrayList<>();

    public boolean includeIn(String stationId){
            return getStationPatterns().stream().anyMatch(p -> stationId.matches(p));
    }

    public QuestStation getStation() {
        return station;
    }

    public void setStation(QuestStation station) {
        this.station = station;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check == null ? Boolean.TRUE.toString() : check;
    }

    public IncludeProcess getProcess() {
        return process;
    }

    public void setProcess(IncludeProcess process) {
        this.process = process;
    }

    public List<String> getStationPatterns() {
        return Collections.unmodifiableList(stationPatterns);
    }

    public void addStationPatterns(String stationPattern) {
        this.stationPatterns.add(stationPattern.replace("*", ".*"));
    }
}
