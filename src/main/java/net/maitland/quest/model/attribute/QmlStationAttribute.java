package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

/**
 * Created by David on 02/03/2017.
 */
public class QmlStationAttribute extends GameStateStringAttribute {


    public QmlStationAttribute() {
        super("qmlStation");
    }

    @Override
    protected String getGameStateValue(Game game) {
        return game.getQuestPath().peek();
    }
}
