package server.game;

import objects.Sendable;

/**
 * Created by peran on 2/7/17.
 *
 */
public class Scoreboard implements Sendable {
    private int team1Score;
    private int team0Score;
    private int maxScore;
    private int[] playerScores;

    Scoreboard(int maxScore, int playerNum) {
        this.maxScore = maxScore;
        team0Score = 0;
        team1Score = 0;
        playerScores = new int[playerNum];
        for (int i = 0; i < playerNum; i++) {
            playerScores[i] = 0;
        }
    }

    boolean scoreReached() {
        return (team1Score >= maxScore || team0Score >= maxScore);
    }

    private void addScore(Player p, int score) {
        playerScores[p.getID()] += score;
        if (p.getTeam() == 0) {
            team0Score += score;
        }
        else {
            team1Score += score;
        }
    }

    void killedPlayer(Player p) {
        addScore(p, 10);
    }

    void killedOrb(Player p) {
        addScore(p, 3);
    }

    void KilledByOrb(Player p) {
        addScore(p, -3);
    }

    public int[] getPlayerScores() {
        return playerScores;
    }

    public String toString() {
        String s = ("Team 1: "+team0Score+ " Team 2: "+team1Score+"\n");
        int[] temp = playerScores.clone();
        int removed = 0;
        while (removed < temp.length) {
            int max = Integer.MIN_VALUE;
            int index = -1;
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] > max) {
                    index = i;
                    max = temp[i];
                }
            }
            if (index != -1) {
                s += "Player " + index + " Score: "+max+"\n";
                temp[index] = Integer.MIN_VALUE;
            }
            else {
                break;
            }
            removed++;
        }
        return s;
    }

    protected Scoreboard clone() {
        Scoreboard sb = new Scoreboard(maxScore, playerScores.length);
        sb.team1Score = team1Score;
        sb.team0Score = team0Score;
        sb.playerScores = playerScores.clone();
        return  sb;
    }
}
