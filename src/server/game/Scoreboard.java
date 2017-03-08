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
    private int[] playerScore;

    public Scoreboard(int maxScore, int playerNum) {
        this.maxScore = maxScore;
        team0Score = 0;
        team1Score = 0;
        playerScore = new int[playerNum];
        for (int i = 0; i < playerNum; i++) {
            playerScore[i] = 0;
        }
    }

    boolean scoreReached() {
        return (team1Score >= maxScore || team0Score >= maxScore);
    }

    private void addScore(Player p, int score) {
        playerScore[p.getID()] += score;
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
        addScore(p, 5);
    }

    public int getPlayerScore(int playerID) {
        return playerScore[playerID];
    }

    public String toString() {
        String s = ("Team 1: "+team0Score+ " Team 2: "+team1Score+"\n");
        int[] temp = playerScore.clone();
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
                s += "Player " + index + " Score: "+max;
                temp[index] = Integer.MIN_VALUE;
            }
            else {
                break;
            }

        }
        return s;
    }
}
