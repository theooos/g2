package server.game;

import objects.Sendable;

/**
 * Created by peran on 2/7/17.
 */
public class Scoreboard implements Sendable{
    private int team1Score;
    private int team2Score;
    private int maxScore;
    private int[] playerScore;

    public Scoreboard(int maxScore, int playerNum) {
        this.maxScore = maxScore;
        team2Score = 0;
        team1Score = 0;
        playerScore = new int[playerNum];
        for (int i = 0; i < playerNum; i++) {
            playerScore[i] = 0;
        }
    }

    public boolean scoreReached() {
        return (team1Score >= maxScore || team2Score >= maxScore);
    }

    private void addScore(int playerID, int score) {
        playerScore[playerID] += score;
    }

    public void killedPlayer(int playerID) {
        addScore(playerID, 10);
    }

    public void killedZombie(int playerID) {
        addScore(playerID, 5);
    }

    public int getPlayerScore(int playerID) {
        return playerScore[playerID];
    }
}
