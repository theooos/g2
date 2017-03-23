package server.game;

import objects.Sendable;
import static server.game.ServerConfig.*;

/**
 * Created by peran on 2/7/17.
 * A class to hold player and team scores
 */
public class Scoreboard implements Sendable {
    private int team1Score;
    private int team0Score;
    private int maxScore;
    private int[] playerScores;

    /**
     * Creates a new scoreboard, scores start on 0
     * @param maxScore the game's score limit
     * @param playerNum the number of players in the game
     */
    Scoreboard(int maxScore, int playerNum) {
        this.maxScore = maxScore;
        team0Score = 0;
        team1Score = 0;
        playerScores = new int[playerNum];
        for (int i = 0; i < playerNum; i++) {
            playerScores[i] = 0;
        }
    }

    /**
     * Checks if the scorelimit has been reached
     * @return whether the score limit has been reached
     */
    boolean scoreReached() {
        return (team1Score >= maxScore || team0Score >= maxScore);
    }

    /**
     * A private method to add a score to a player and team
     * @param p the player which scored
     * @param score the score to inc
     */
    private void addScore(Player p, int score) {
        playerScores[p.getID()] += score;
        if (p.getTeam() == 0) {
            team0Score += score;
        }
        else {
            team1Score += score;
        }
    }

    /**
     * When a player kills another player
     * @param p the player which got the kill
     */
    void killedPlayer(Player p) {
        addScore(p, PLAYER_KILLED_SCORE);
    }

    /**
     * When a player kills an orb
     * @param p the player which got the kill
     */
    void killedOrb(Player p) {
        addScore(p, ORB_KILLED_SCORE);
    }

    /**
     * When a player is killed by an orb, subtract the points
     * @param p the player which died
     */
    void killedByOrb(Player p) {
        addScore(p, KILLED_BY_ORB_SCORE);
    }

    /**
     * Gets the array of player scores
     * @return the array of player scores, unsorted.  The players are stored by their ID
     */
    public int[] getPlayerScores() {
        return playerScores;
    }

    /**
     * A too string method of the scoreboard
     * @return A string of the scoreboard, sorted by scores
     */
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

    /**
     * Clones a given scoreboard
     * @return an exact copy of the current scoreboard
     */
    protected Scoreboard clone() {
        Scoreboard sb = new Scoreboard(maxScore, playerScores.length);
        sb.team1Score = team1Score;
        sb.team0Score = team0Score;
        sb.playerScores = playerScores.clone();
        return  sb;
    }

    /**
     * Checks if two scoreboards are equal
     * @param sb the scoreboard to compare
     * @return whether equal or not
     */
    boolean equals(Scoreboard sb) {
        if (sb.getPlayerScores().length != playerScores.length) return false;
        if (sb.maxScore != maxScore) return false;
        for (int i = 0; i < playerScores.length; i++) {
            if (sb.playerScores[i] != playerScores[i]) return false;
        }
        return true;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public int getTeam0Score() {
        return team0Score;
    }
}
