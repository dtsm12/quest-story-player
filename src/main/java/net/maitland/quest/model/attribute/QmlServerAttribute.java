package net.maitland.quest.model.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * Created by David on 04/03/2017.
 */
public class QmlServerAttribute extends SimpleAttribute implements InternalAttribute {

    public QmlServerAttribute() {
        super("qmlServer", "true");
    }
}
