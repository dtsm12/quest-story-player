package net.maitland.quest.jackson;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import net.maitland.quest.model.QuestStation;

/**
 * Created by David on 18/12/2016.
 */
public class BackStationAwareObjectIdResolver extends SimpleObjectIdResolver {

    @Override
    public void bindItem(ObjectIdGenerator.IdKey idKey, Object o) {
        super.bindItem(idKey, o);
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey idKey) {
        if (idKey.key.equals(QuestStation.BACK_STATION_ID)) {
            return QuestStation.getBackStation();
        } else {
            return super.resolveId(idKey);
        }
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object o) {
        return this;
    }

    @Override
    public boolean canUseFor(ObjectIdResolver objectIdResolver) {
        return true;
    }

}
