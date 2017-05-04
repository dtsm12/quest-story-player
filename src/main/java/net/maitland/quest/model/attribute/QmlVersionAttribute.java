package net.maitland.quest.model.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * Created by David on 04/03/2017.
 */
public class QmlVersionAttribute extends SimpleAttribute implements InternalAttribute {

    public QmlVersionAttribute() {
        super("qmlVersion", "2");
    }
}
