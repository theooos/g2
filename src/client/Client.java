package client;

import client.audio.Audio;
import client.graphics.GameManager;
import client.graphics.StartScreenRenderer;
import client.graphics.TextRenderer;
import client.graphics.TextureLoader;
import networking.Connection;
import objects.GameData;
import objects.InitGame;
import objects.Sendable;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Client {

    public enum Mode {SPLASH, GAME}
    private Mode currentMode = Mode.SPLASH;

    private Connection connection;
    private ClientReceiver clientReceiver;
    private int playerID;

    private TextRenderer textRenderer;
    private StartScreenRenderer startScreen;
    private GameManager gameRenderer;

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
                case SPLASH:
                    startScreen.run();
                    break;
                case GAME:
                    // TODO Make sure this doesn't happen every loop.
                    changeDisplaySettings();
                    gameRenderer.run();
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

            textRenderer = new TextRenderer();

            startScreen = new StartScreenRenderer(e -> establishConnection());

            Audio.init();
            Audio.volume = Audio.Volume.LOW;
            Audio.INTERFACEBACKGROUND.play();

        } catch (LWJGLException le) {
            System.err.println("Game exiting - exception in initialization:");
            le.printStackTrace();
            running = false;
        }
    }

    private void beginGame(Sendable s) {
        GameData gameData = new GameData((InitGame) s);
        clientReceiver.setGameData(gameData);
        gameRenderer = new GameManager(gameData, connection, playerID);
        currentMode = Mode.GAME;
        Audio.INTERFACEBACKGROUND.stopClip();
        Audio.COUNTDOWN.play();
        Audio.GAMEMUSIC.pause(4);
    }

    private void establishConnection() {
        try {
            connection = new Connection();
            clientReceiver = new ClientReceiver(connection);
            connection.addFunctionEvent("InitGame", this::beginGame);
            connection.addFunctionEvent("String",this::getID);
            connection.addFunctionEvent("LobbyData",startScreen::setupLobby);
        } catch (IOException e) {
            System.err.println("Failed to make connection.");
        }
    }

    private void getID(Object o) {
        String information = o.toString();
        String t = information.substring(0, 2);

        switch (t) {
            case "ID":
                String idS = information.substring(2);
                int id = Integer.parseInt(idS);
                playerID = id;
                clientReceiver.setID(playerID);
                break;
        }
    }

    public static void main(String argv[]) {
        new Client();
        System.exit(0);
    }
}
