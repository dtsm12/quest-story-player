package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

import java.util.Deque;

/**
 * Created by David on 02/03/2017.
 */
public class QmlLastStationAttribute extends GameStateAttribute {


    public QmlLastStationAttribute() {
        super("qmlLastStation");
    }

    @Override
    protected String getGameStateValue(Game game) {
        Deque<String> questPath = game.getQuestPath();
        String current =  questPath.pop();
        String last = questPath.peek();
        questPath.push(current);
        return last;
    }
}
