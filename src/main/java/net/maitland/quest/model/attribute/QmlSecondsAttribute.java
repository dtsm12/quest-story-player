package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

import java.util.Date;

/**
 * Created by David on 28/02/2017.
 */
public class QmlSecondsAttribute extends GameStateAttribute {

    public static final int MAX_SECONDS = 30000;

    public QmlSecondsAttribute() {
        super("qmlSeconds");
    }

    @Override
    protected String getGameStateValue(Game game) {

        long startTime = Long.valueOf(game.getAttributeValue(Game.QML_START_TIME)).longValue();
        long now = new Date().getTime();
        int qmlSeconds = new Long((now - startTime)/1000).intValue();

        return String.valueOf(Math.min(qmlSeconds, MAX_SECONDS));
    }
}
