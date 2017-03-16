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

    public void preVisit(Game game) throws QuestStateException {
        this.visit(game, false);
    }

    public void postVisit(Game game) throws QuestStateException {
        this.visit(game, true);
    }

    protected void visit(Game game, boolean postVisit) throws QuestStateException {

        VirtualQuestSection applicableQuestSection = getApplicableQuestSection(game);
        game.updateState((postVisit ? applicableQuestSection.getPostVisitAttributes() : applicableQuestSection.getPreVisitAttributes()));
    }

    public String getText(Game game) throws QuestStateException {

        List<Text> texts = getApplicableQuestSection(game).getTexts();
        return game.toStateText(texts);
    }

    public List<Choice> getChoices(Game game) throws QuestStateException {

        VirtualQuestSection questSection = getApplicableQuestSection(game);
        List<Choice> filteredChoices = new ArrayList<>();

        for (Choice c : questSection.getChoices()) {
            if (game.check(c)) {
                filteredChoices.add(c);
            }
        }

        return filteredChoices;
    }

    public boolean includeIn(String stationId) {

        boolean include = this.stationIncludeRules != null && this.stationIncludeRules.includeIn(stationId);
        return include;
    }

    protected VirtualQuestSection getApplicableQuestSection(Game game) throws QuestStateException {

        VirtualQuestSection virtualQuestSection = new VirtualQuestSection();

        // get station section
        mergeApplicableQuestSections(game, virtualQuestSection, this, null);

        //add any applicable includes
        this.includedStations.stream().filter(is -> game.check(is)).forEach(is -> mergeApplicableQuestSections(game, virtualQuestSection, is.getStation(), is.getProcess()));

        return virtualQuestSection;
    }

    protected void mergeApplicableQuestSections(Game game, VirtualQuestSection virtualQuestSection, QuestSection questSection, IncludeProcess insertPosition) throws QuestStateException {

        mergeApplicableQuestSections(virtualQuestSection, questSection, insertPosition);

        QuestSection applicableQuestSection = null;
        if (this.getConditions().size() > 0) {

            for (IfSection i : this.getConditions()) {
                if (game.check(i)) {
                    applicableQuestSection = i;
                    break;
                }
            }

            if (applicableQuestSection == null) {
                applicableQuestSection = this.getElseCondition();
            }
        }

        if (applicableQuestSection != null) {
            mergeApplicableQuestSections(virtualQuestSection, applicableQuestSection, insertPosition);
        }

        this.log.debug("Applicable section for stationId '{}' is {}", this.getId(), questSection.getClass().getName());
    }

    protected void mergeApplicableQuestSections(VirtualQuestSection virtualQuestSection, QuestSection questSection, IncludeProcess insertPosition) {
        virtualQuestSection.add(questSection, insertPosition);
    }
}

