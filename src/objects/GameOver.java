package objects;

import server.game.Scoreboard;

/**
 * Created by theo on 18/03/2017.
 * Used to notify the client of game over
 */
public class GameOver implements Sendable {

    private Scoreboard scoreboard;

    /**
     * Ends the game
     * @param scoreboard the final score
     */
    public GameOver(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
