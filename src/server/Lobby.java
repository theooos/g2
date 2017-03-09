package server;

import networking.Connection;
import server.game.Game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by peran on 25/01/17.
 * A lobby class which creates and handles games
 */
class Lobby {
    private int maxSize;
    private int minSize;
    private ArrayList<Connection> players;
    private boolean countdownRunning;
    private int countdown;
    private Timer t;
    private int map;
    private boolean gameRunning;

    Lobby(int maxSize) {
        int mapMax = 3;
        countdownRunning = false;
        players = new ArrayList<>();
        this.maxSize = maxSize;
        minSize = maxSize/2;
        t = new Timer();
        Random rand = new Random();
        map = rand.nextInt(mapMax);
        //map = 2;
        System.out.println(map);
        gameRunning = false;
    }

    /**
     * checks to see whether the lobby has reached maximum
     * @return if the lobby is full or not
     */
    boolean isFull() {
        return players.size() >= maxSize;
    }

    /**
     * Adds a new player to the lobby
     * @param c the connected player
     */
    void addConnection(Connection c) {
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
            countdown = 0;
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
    private void stopCountdown() {
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
        gameRunning = true;
        new Thread(new Game(players, maxSize, map)).start();
    }

    boolean isGameRunning() {
        return gameRunning;
    }

}
