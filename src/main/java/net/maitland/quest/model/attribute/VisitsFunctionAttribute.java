package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;
import net.maitland.quest.model.StateAttribute;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by David on 28/02/2017.
 */
public class VisitsFunctionAttribute extends TemplateAttribute {

    public VisitsFunctionAttribute() {
        super("visits\\(([^\\(\\)]+)\\)", "%1$s");
    }

    @Override
    protected String processTemplateValues(Object[] values, Game game) {
        String stationName = (String) values[0];
        Collection stations;

        if("*".equals(stationName))
        {
            stations = game.getQuestPath();
        }
        else
        {
            stations = game.getQuestPath().stream().filter(s -> s.equals(stationName)).collect(Collectors.toList());
        }

        return String.valueOf(stations.size());
    }
}
