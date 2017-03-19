package server;

import networking.Connection;
import objects.LobbyData;
import objects.Sendable;
import server.game.Game;

import java.util.*;

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
    private int mapID;
    private boolean gameRunning;

    Lobby(int maxSize) {
        int mapMax = 3;
        countdownRunning = false;
        players = new ArrayList<>();
        this.maxSize = maxSize;
        minSize = maxSize/2;
        t = new Timer();
        Random rand = new Random();
        mapID = rand.nextInt(mapMax);
        //mapID = 2;
        System.out.println(mapID);
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
        if(players.add(c)){
            sendAllNewLobbyInfo();
            if (players.size() >= minSize) {
                startCountdown();
            }
        }
    }

    /**
     * This sends all connected players all the updated lobby information.
     */
    private void sendAllNewLobbyInfo() {
        ArrayList<Integer> playerIDs = new ArrayList<>();
        for(int i = 0; i < players.size(); i++){
            playerIDs.add(i);
        }
        sendToAll(new LobbyData(playerIDs,mapID));
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
        sendToAll(new objects.String(s));
    }

    private void sendToAll(Sendable sendable){
        for (Connection c: players) {
            c.send(sendable);
        }
    }

    /**
     * starts a game, is called when countdown runs out.
     */
    private void startGame() {
        msgToAllConnected("Game loading....");
        gameRunning = true;
        Game game = new Game(players, maxSize, mapID);
        game.run();
    }

    boolean isGameRunning() {
        return gameRunning;
    }

}
