package server;

import networking.Connection;
import objects.InitPlayer;
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
    private HashMap<Integer, Connection> connections;
    private boolean[] used;
    private InitPlayer[] players;
    private boolean countdownRunning;
    private int countdown;
    private Timer t;
    private int mapID;
    private boolean gameRunning;

    /**
     * Creates a new lobby for players to connect too
     * @param maxSize the maximum number of players this lobby can hold
     */
    Lobby(int maxSize) {
        //the max number of maps the server has access to
        int mapMax = 3;

        countdownRunning = false;
        connections = new HashMap<>();

        //used to check if a player has occupied that ID
        used = new boolean[maxSize];
        for (int i = 0; i < used.length; i++) {
            used[i] = false;
        }
        players = new InitPlayer[maxSize];
        for (int i = 0; i < players.length; i++) {
            players[i] = new InitPlayer(i, new objects.String("Test"), true, i%2);
        }

        this.maxSize = maxSize;
        minSize = maxSize/2;
        t = new Timer();
        Random rand = new Random();
        mapID = rand.nextInt(mapMax);
        System.out.println("Map ID: " +mapID);
        gameRunning = false;
    }

    /**
     * checks to see whether the lobby has reached maximum
     * @return if the lobby is full or not
     */
    boolean isFull() {
        return connections.size() >= maxSize;
    }

    /**
     * Adds a new player to the lobby
     * @param c the connected player
     */
    void addConnection(Connection c) {
        for (int i = 0; i < used.length; i++) {
            if (!used[i]) {
                connections.put(i, c);
                players[i] = new InitPlayer(i, new objects.String("test"), false, i%2);
                c.send(players[i]);
                sendAllNewLobbyInfo();
                if (connections.size() >= minSize) {
                    startCountdown();
                }
                used[i] = true;
                break;
            }
        }
    }

    /**
     * This sends all connected connections all the updated lobby information.
     */
    private void sendAllNewLobbyInfo() {
        InitPlayer[] p = players.clone();
        sendToAll(new LobbyData(p,mapID));
    }

    /**
     * starts and runs the countdown to start the game
     */
    private void startCountdown() {
        if (!countdownRunning) {
            countdown = 0;
            t = new Timer();
            countdownRunning = true;
            msgToAllConnected("Minimum number of connections is reached, countdown starting");

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
     * sends the string to all connections in the lobby
     * @param s the string to be sent
     */
    private void msgToAllConnected(String s) {
        sendToAll(new objects.String(s));
    }

    private void sendToAll(Sendable sendable){
        for (Connection c: connections.values()) {
            c.send(sendable);
        }
    }

    /**
     * starts a game, is called when countdown runs out.
     */
    private void startGame() {
        msgToAllConnected("Game loading....");
        gameRunning = true;
        Game game = new Game(connections, maxSize, mapID, new LobbyData(players.clone(),mapID));
        game.run();
    }

    boolean isGameRunning() {
        return gameRunning;
    }

}
