package net.maitland.quest.model.attribute;

import net.maitland.quest.model.Game;

/**
 * Created by David on 02/03/2017.
 */
public abstract class GameStateAttribute extends TemplateAttribute {

    public GameStateAttribute(String name) {
        super("\\["+name+"\\]", "["+name+"]");
    }

    @Override
    protected String processTemplateValues(Object[] r, Game game, boolean isCheck) {
        return getGameStateValue(game);
    }

    protected String processTemplateValues(Object[] r, Game game) {
        return processTemplateValues(r, game, true);
    }

    protected abstract String getGameStateValue(Game game);
}
