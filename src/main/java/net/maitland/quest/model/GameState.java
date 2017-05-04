package net.maitland.quest.model;

/**
 * Created by David on 03/04/2017.
 */
public class GameState {

    private Game game;
    private GameStation gameStation;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameStation getGameStation() {
        return gameStation;
    }

    public void setGameStation(GameStation gameStation) {
        this.gameStation = gameStation;
    }
}
