package server;

import networking.Connection_Server;
import objects.InitPlayer;
import objects.LobbyData;
import objects.Sendable;
import server.game.Game;

import java.io.*;
import java.util.*;

import static server.game.ServerConfig.NAME_LOCAL;

/**
 * Created by peran on 25/01/17.
 * A lobby class which creates and handles games
 */
class Lobby {
    private int maxSize;
    private int minSize;
    private HashMap<Integer, Connection_Server> connections;
    private boolean[] used;
    private InitPlayer[] players;
    private boolean countdownRunning;
    private int countdown;
    private int maxCountdown;
    private Timer t;
    private int mapID;
    private boolean gameRunning;

    /**
     * Creates a new lobby for players to connect too
     * @param maxSize the maximum number of players this lobby can hold
     * @param countdown the countdown after min players is reached to start the game
     */
    Lobby(int maxSize, int countdown) {
        //the max number of maps the server has access to
        int mapMax = 3;

        maxCountdown = countdown;
        countdownRunning = false;
        connections = new HashMap<>();

        Random rand = new Random();

        //used to check if a player has occupied that ID
        used = new boolean[maxSize];
        for (int i = 0; i < used.length; i++) {
            used[i] = false;
        }

        //Set up the name files for name selection and add names to ArrayLists
        ArrayList<String> enclaveNames = new ArrayList<>();
        ArrayList<String> landscapeNames = new ArrayList<>();

        try {
            BufferedReader eNames;
            BufferedReader lNames;
            if (NAME_LOCAL) {
                eNames = new BufferedReader(new FileReader(Lobby.class.getResource("../EnclaveNames.txt").getFile()));
                lNames = new BufferedReader(new FileReader(Lobby.class.getResource("../LandscapersNames.txt").getFile()));
            }
            else {
                String LOCAL_PATH = new File("").getAbsolutePath()+"/maps/";
                eNames = new BufferedReader(new FileReader(LOCAL_PATH + "EnclaveNames.txt"));
                lNames = new BufferedReader(new FileReader(LOCAL_PATH + "LandscapersNames.txt"));
            }
            String line = eNames.readLine();
            while (line != null) {
                enclaveNames.add(line);
                line = eNames.readLine();
            }
            eNames.close();
            line = lNames.readLine();
            while (line != null) {
                landscapeNames.add(line);
                line = lNames.readLine();
            }
            lNames.close();
        } catch (FileNotFoundException e) {
            System.err.println("Failed to find name files\n"+e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //give each player a name
        players = new InitPlayer[maxSize];
        for (int i = 0; i < players.length; i++) {
            String name;
            if (i%2 == 0) {
                name = enclaveNames.get(rand.nextInt(enclaveNames.size()));
            }
            else {
                name = landscapeNames.get(rand.nextInt(landscapeNames.size()));
            }
            players[i] = new InitPlayer(i, new objects.String(name), true, i%2);
        }

        enclaveNames.clear();
        landscapeNames.clear();

        this.maxSize = maxSize;
        minSize = maxSize/2;
        t = new Timer();
        //mapID = 2;
        System.out.println(mapID);
        mapID = rand.nextInt(mapMax) + 1;
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
    void addConnection(Connection_Server c) {
        for (int i = 0; i < used.length; i++) {
            if (!used[i]) {
                c.setID(i);
                connections.put(i, c);
                objects.String name = players[i].getName();
                players[i] = new InitPlayer(i, name, false, i%2);
                try {
                    c.send(players[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            countdown = maxCountdown;
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
        boolean connectionDied = false;
        for (Connection_Server c: connections.values()) {
            try {
                c.send(sendable);
            } catch (Exception e) {
                int ID = c.getID();
                connections.remove(c);
                used[ID] = false;
                players[ID] = new InitPlayer(ID, players[ID].getName(), false, players[ID].getTeam());
                connectionDied = true;
                break;
            }
        }
        if(connectionDied) {
            sendAllNewLobbyInfo();
            if (connections.size() < minSize) stopCountdown();
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
