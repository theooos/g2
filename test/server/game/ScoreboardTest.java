package server.game;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static server.game.ServerConfig.KILLED_BY_ORB_SCORE;
import static server.game.ServerConfig.ORB_KILLED_SCORE;
import static server.game.ServerConfig.PLAYER_KILLED_SCORE;

/**
 * Created by peran on 3/20/17.
 * Used to test the Scoreboard class on the server
 */
class ScoreboardTest {
    private Scoreboard scoreboard;
    private Player l0;
    private Player l1;
    private Player e0;
    private Player e1;

    @BeforeEach
    void setUp() {
        //creates the scoreboard
        scoreboard = new Scoreboard(20, 4);
        //creates players to be tested
        l0 = new Player(null, null, 1, 0, null, null, 0);
        l1 = new Player(null, null, 1, 0, null, null, 1);
        e0 = new Player(null, null, 0, 0, null, null, 2);
        e1 = new Player(null, null, 0, 0, null, null, 3);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        //doesn't need to tear down anything in scoreboard
    }

    @org.junit.jupiter.api.Test
    void scoreReached() {
        scoreboard.killedPlayer(l0);
        assertFalse(scoreboard.scoreReached());
        scoreboard.killedPlayer(e0);
        assertFalse(scoreboard.scoreReached());
        scoreboard.killedPlayer(l1);
        assertTrue(scoreboard.scoreReached());
        scoreboard.killedByOrb(l1);
        assertFalse(scoreboard.scoreReached());
        scoreboard.killedOrb(l1);
        assertTrue(scoreboard.scoreReached());
    }

    @org.junit.jupiter.api.Test
    void killedPlayer() {
        scoreboard.killedPlayer(l1);
        scoreboard.killedPlayer(l1);
        int score = scoreboard.getTeam1Score();
        assertTrue(score == 2*PLAYER_KILLED_SCORE);
    }

    @org.junit.jupiter.api.Test
    void killedOrb() {
        scoreboard.killedOrb(l1);
        scoreboard.killedOrb(l1);
        int score = scoreboard.getTeam1Score();
        assertTrue(score == 2*ORB_KILLED_SCORE);
    }

    @org.junit.jupiter.api.Test
    void killedByOrb() {
        scoreboard.killedOrb(l1);
        scoreboard.killedByOrb(l1);
        int score = scoreboard.getTeam1Score();
        assertTrue(score == (ORB_KILLED_SCORE+KILLED_BY_ORB_SCORE));
    }

    @org.junit.jupiter.api.Test
    void getPlayerScores() {
        scoreboard.killedOrb(e1);
        scoreboard.killedPlayer(e0);
        scoreboard.killedPlayer(l1);
        scoreboard.killedPlayer(l0);
        scoreboard.killedOrb(l0);
        int[] scores = scoreboard.getPlayerScores();
        assertTrue(scores[e1.getID()] == ORB_KILLED_SCORE);
        assertTrue(scores[e0.getID()] == PLAYER_KILLED_SCORE);
        assertTrue(scores[l1.getID()] == PLAYER_KILLED_SCORE);
        assertTrue(scores[l0.getID()] == ORB_KILLED_SCORE+PLAYER_KILLED_SCORE);


    }

    @org.junit.jupiter.api.Test
    void toStringTest() {
        scoreboard.killedPlayer(e0);
        scoreboard.killedPlayer(e1);
        scoreboard.killedOrb(e1);
        scoreboard.killedOrb(l1);
        String s = ("Team 1: 23 Team 2: 3\n");

        s += "Player " + e1.getID() + " Score: 13\n";
        s += "Player " + e0.getID() + " Score: 10\n";
        s += "Player " + l1.getID() + " Score: 3\n";
        s += "Player " + l0.getID() + " Score: 0\n";

        assertTrue(s.equals(scoreboard.toString()));

        s = ("Team 1: 23 Team 2: 3\n");

        s += "Player " + e0.getID() + " Score: 10\n";
        s += "Player " + l1.getID() + " Score: 3\n";
        s += "Player " + l0.getID() + " Score: 0\n";
        s += "Player " + e1.getID() + " Score: 13\n";

        assertFalse(s.equals(scoreboard.toString()));
    }

    @org.junit.jupiter.api.Test
    void equals() {
        Scoreboard sb = new Scoreboard(20, 4);
        sb.killedPlayer(e0);
        scoreboard.killedPlayer(e0);
        assertTrue(sb.equals(scoreboard));
        sb.killedPlayer(e0);
        assertFalse(sb.equals(scoreboard));
    }

    @org.junit.jupiter.api.Test
    void cloneTest() {
        Scoreboard sb = scoreboard.clone();
        assertTrue(sb.equals(scoreboard));
    }

    @org.junit.jupiter.api.Test
    void getTeam1Score() {
        scoreboard.killedPlayer(e1);
        assertTrue(scoreboard.getTeam1Score() == 0);
        scoreboard.killedPlayer(l1);
        assertTrue(scoreboard.getTeam1Score() == PLAYER_KILLED_SCORE);
        scoreboard.killedPlayer(l0);
        assertTrue(scoreboard.getTeam1Score() == PLAYER_KILLED_SCORE*2);
        scoreboard.killedOrb(l0);
        assertTrue(scoreboard.getTeam1Score() == PLAYER_KILLED_SCORE*2+ORB_KILLED_SCORE);
    }

    @org.junit.jupiter.api.Test
    void getTeam0Score() {
        scoreboard.killedPlayer(l1);
        assertTrue(scoreboard.getTeam0Score() == 0);
        scoreboard.killedPlayer(e1);
        assertTrue(scoreboard.getTeam0Score() == PLAYER_KILLED_SCORE);
        scoreboard.killedOrb(e0);
        assertTrue(scoreboard.getTeam0Score() == PLAYER_KILLED_SCORE+ORB_KILLED_SCORE);
        scoreboard.killedOrb(e1);
        assertTrue(scoreboard.getTeam0Score() == PLAYER_KILLED_SCORE+2*ORB_KILLED_SCORE);
    }

}