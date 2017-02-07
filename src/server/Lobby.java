package server;

import networking.Connection;
import server.game.Game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by peran on 25/01/17.
 */
public class Lobby {
    private int maxSize;
    private int minSize;
    private ArrayList<Connection> players;
    private boolean countdownRunning;
    private int countdown;
    private Timer t;
    private Game game;
    private int map;



    public Lobby(int maxSize) {
        countdownRunning = false;
        players = new ArrayList<>();
        this.maxSize = maxSize;
        minSize = maxSize/2;
        t = new Timer();
        map = maxSize;
    }

    /**
     * checks to see whether the lobby has reached maximum
     * @return if the lobby is full or not
     */
    public boolean isFull() {
        if (players.size() < maxSize) return false;
        else return true;
    }

    /**
     * Adds a new player to the lobby
     * @param c the connected player
     */
    public void addConnection(Connection c) {
        players.add(c);
        msgToAllConnected("Player connected");
        c.send(new objects.String("You are in a "+maxSize+" player lobby with "+players.size()+" players in it"));
        if (players.size() >= minSize) {
            startCountdown();
        }

    }

    /**
     * starts and runs the countdown to start the game
     */
    private void startCountdown() {
        if (!countdownRunning) {
            countdown = 60;
            t = new Timer();
            countdownRunning = true;
            msgToAllConnected("Minimum number of players is reached, countdown starting");

            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    msgToAllConnected("Game starting in "+countdown+"s");
                    countdown--;

                    //stops the countdown when the timer has run out
                    if (countdown < 0) {
                        stopCountdown();
                        startGame();
                    }
                }
            }, 1000,1000);
        }
    }

    /**
     * stops the countdown and resets timer to 60s
     */
    public void stopCountdown() {
        t.cancel();
        t.purge();
        countdownRunning = false;
    }

    /**
     * sends the string to all players in the lobby
     * @param s the string to be sent
     */
    private void msgToAllConnected(String s) {
        for (Connection c: players) {
            c.send(new objects.String(s));
        }
    }

    /**
     * starts a game, is called when countdown runs out.
     */
    private void startGame() {
        msgToAllConnected("Game loading....");
        game = new Game(players, maxSize, map);
    }

}
