package objects;

import server.game.Scoreboard;

/**
 * Created by theo on 18/03/2017.
 */
public class GameOver implements Sendable {

    private Scoreboard scoreboard;

    public GameOver(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
