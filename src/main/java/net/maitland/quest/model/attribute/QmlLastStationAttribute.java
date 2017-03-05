package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

import java.util.Deque;

/**
 * Created by David on 02/03/2017.
 */
public class QmlLastStationAttribute extends GameStateStringAttribute {


    public QmlLastStationAttribute() {
        super("qmlLastStation");
    }

    @Override
    protected String getGameStateValue(Game game) {
        String last = "nolaststation";
        Deque<String> questPath = game.getQuestPath();
        if(questPath.size() > 1) {
            String current = questPath.pop();
            last = questPath.peek();
            questPath.push(current);
        }
        return last;
    }
}
