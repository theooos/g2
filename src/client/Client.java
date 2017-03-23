package client;

import client.audio.Audio;
import client.audio.AudioManager;
import client.graphics.*;
import networking.Connection_Client;
import objects.GameData;
import objects.InitGame;
import objects.InitPlayer;
import objects.Sendable;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static client.ClientSettings.AMBIENT_VOL;
import static org.lwjgl.opengl.GL11.*;

public class Client {

    public enum Mode {MAIN_MENU, GAME}
    private Mode currentMode = Mode.MAIN_MENU;

    private Connection_Client connection;
    private ClientReceiver clientReceiver;
    private int playerID;

    private StartScreenRenderer startScreen;
    private GameManager gameManager;

    private TextRenderer[] textRenderers = new TextRenderer[3];

    private boolean running = true;

    private Client() {
        initialise();
        loop();
    }

    private void loop() {
        while (!Display.isCloseRequested() && running) {
            // clear screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            // let subsystem paint
            switch (currentMode) {
                case MAIN_MENU:
                    glEnable(GL_TEXTURE_2D);
                    glColor4f(1,1,1,1);
                    startScreen.run();
                    break;
                case GAME:
                    // TODO Make sure this doesn't happen every loop.
                    changeDisplaySettings();
                    gameManager.run();
                    break;
                default:
                    System.err.println("Not in a mode.");
            }

            // update window contents
            Display.update();
            Display.sync(60);
        }
    }

    private void changeDisplaySettings() {
        GL11.glDisable(GL_TEXTURE_2D); // Enable Texture Mapping
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, ClientSettings.SCREEN_WIDTH, 0, ClientSettings.SCREEN_HEIGHT, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void initialise() {
        // initialize the window beforehand
        try {
            //setDisplayMode();
            Display.setDisplayMode(new DisplayMode(ClientSettings.SCREEN_WIDTH, ClientSettings.SCREEN_HEIGHT));
            Display.setTitle(ClientSettings.WINDOW_TITLE);
            Display.setFullscreen(ClientSettings.FULLSCREEN);
            Display.create();

            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
            GL11.glClearDepth(1.0f); // Depth Buffer Setup
            GL11.glDisable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDepthMask(false);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, ClientSettings.SCREEN_WIDTH, 0, ClientSettings.SCREEN_HEIGHT, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            TextureLoader.initialise();
            textRenderers[0] = new TextRenderer(20);
            textRenderers[1] = new TextRenderer(25);
            textRenderers[2] = new TextRenderer(60);

            SettingsRenderer.initialise();

            startScreen = new StartScreenRenderer(e -> establishConnection());

            Audio.init();
            AudioManager.playAmbiance();
        } catch (LWJGLException le) {
            System.err.println("Game exiting - exception in initialization:");
            le.printStackTrace();
            running = false;
        }
    }

    private void beginGame(Sendable s) {
        GameData gameData = new GameData((InitGame) s);
        clientReceiver.setGameData(gameData);
        gameManager = new GameManager(gameData, connection, playerID, textRenderers,this::endGame);
        currentMode = Mode.GAME;
        startScreen.setCurrentScreen(StartScreenRenderer.Screen.MAIN);
        AudioManager.playGameStart();
        AudioManager.playMusic();
    }

    private void establishConnection() {
        try {
            connection = new Connection_Client(this);
            connection.initialise();
            clientReceiver = new ClientReceiver(connection);
            connection.addFunctionEvent("InitGame", this::beginGame);
            connection.addFunctionEvent("LobbyData",startScreen::setupLobby);
            connection.addFunctionEvent("InitPlayer", this::setupMe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupMe(Sendable s) {
        playerID = ((InitPlayer) s).getID();
    }

    private void endGame(Object o) {
        returnToMainMenu();
    }

    public void returnToMainMenu(){
        currentMode = Mode.MAIN_MENU;
        clientReceiver = null;
        connection = null;
        gameManager = null;
    }

    public static void main(String argv[]) {
        new Client();
        System.exit(0);
    }
}
