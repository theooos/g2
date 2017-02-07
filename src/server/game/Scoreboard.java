package server.game;

/**
 * Created by peran on 2/7/17.
 */
public class Scoreboard {
    private int team1Score;
    private int team2Score;
    private int maxScore;

    public Scoreboard(int maxScore) {
        this.maxScore = maxScore;
        team2Score = 0;
        team1Score = 0;
    }

    public boolean scoreReached() {
        return (team1Score >= maxScore || team2Score >= maxScore);
    }
}
