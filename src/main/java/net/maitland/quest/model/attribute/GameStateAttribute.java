package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

/**
 * Created by David on 02/03/2017.
 */
public abstract class GameStateAttribute extends SimpleAttribute {

    public GameStateAttribute(String name) {
        super(name, name);
    }

    @Override
    public String replace(String value, Game game, boolean isCheck) {
        String result = value;
        if(value.contains(this.getName())) {
            setValue(getGameStateValue(game));
            result = super.replace(value, game, isCheck);
        }
        return result;
    }

    protected abstract String getGameStateValue(Game game);
}
