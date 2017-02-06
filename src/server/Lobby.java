package server;

import networking.Connection;
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



    public Lobby(int maxSize) {
        countdownRunning = false;
        players = new ArrayList<>();
        this.maxSize = maxSize;
        minSize = maxSize/2;
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
        sendToAllConnected("Player connected");
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

            countdownRunning = true;
            sendToAllConnected("Minimum number of players is reached, countdown starting");
            Timer t = new Timer();

            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sendToAllConnected("Game starting in "+countdown+"s");
                    countdown--;
                    //stops the countdown when the timer has run out
                    if (countdown < 0) {
                        t.cancel();
                        t.purge();
                        startGame();
                    }
                }
            }, 1000,1000);
        }
    }

    /**
     * sends the string to all players in the lobby
     * @param s the string to be sent
     */
    private void sendToAllConnected(String s) {
        for (Connection c: players) {
            c.send(new objects.String(s));
        }
    }

    /**
     * starts a game, is called when countdown runs out.
     */
    private void startGame() {
        sendToAllConnected("Game loading....");
    }

}
