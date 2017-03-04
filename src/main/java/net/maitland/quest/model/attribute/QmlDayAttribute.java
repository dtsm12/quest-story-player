package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 28/02/2017.
 */
public class QmlDayAttribute extends GameStateAttribute {

    private final static String DAY_FORMAT = "EEEEEE";
    private DateFormat dateFormat;

    public QmlDayAttribute() {
        super("qmlDay");
    }

    @Override
    protected String getGameStateValue(Game game) {
        return getDateFormat().format(new Date());
    }

    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(DAY_FORMAT);
        }
        return dateFormat;
    }
}
