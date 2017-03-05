package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 04/03/2017.
 */
public class QmlTimeAttribute extends GameStateStringAttribute {

    private final static String DAY_FORMAT = "HH:mm:ss";
    private DateFormat dateFormat;

    public QmlTimeAttribute() {
        super("qmlTime");
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
