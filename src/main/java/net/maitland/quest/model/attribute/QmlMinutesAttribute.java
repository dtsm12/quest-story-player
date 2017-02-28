package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

import java.util.Date;

/**
 * Created by David on 28/02/2017.
 */
public class QmlMinutesAttribute extends TemplateAttribute {

    public static final int MAX_MINUTES = 500;

    public QmlMinutesAttribute() {
        super("qmlMinutes", "%1$s");
    }

    @Override
    protected String processTemplateValues(Object[] r, Game game) {

        long startTime = Long.valueOf(game.getAttributeValue(Game.QML_START_TIME)).longValue();
        long now = new Date().getTime();
        int qmlMinutess = new Long((now - startTime)/(1000*60)).intValue();

        return String.valueOf(Math.min(qmlMinutess, MAX_MINUTES));
    }
}
