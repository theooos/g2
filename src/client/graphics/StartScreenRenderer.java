package client.graphics;

import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import objects.LobbyData;
import objects.Sendable;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.function.Consumer;

/**
 * Holds all the
 */
public class StartScreenRenderer {

    private enum Screen {MAIN, ABOUT, LOADING, LOBBY}

    private Screen currentScreen = Screen.MAIN;

    private InterfaceTexture background = new InterfaceTexture(ISprite.BACKGROUND);
    private InterfaceTexture about;
    private InterfaceTexture instructions;
    private InterfaceTexture about_text;
    private InterfaceTexture go_back;
    private InterfaceTexture join_game;

    private static int PHASE = 0;

    private static Layer interfaceLayer = new Layer();
    private static Layer aboutLayer = new Layer();
    private static Layer loadingLayer = new Layer();

    private LobbyData lobbyData;

    private boolean hasClicked = false;

    private Consumer<Void> connectFunction;

    public StartScreenRenderer(Consumer<Void> connectFunction) {
        this.connectFunction = connectFunction;
        background.setRatio(0.5f);
        readyInterfaceLayer();
        readyAboutLayer();
        readyLoadingLayer();
    }

    public void run() {
        switch (currentScreen) {
            case MAIN:
                renderInterface();
                handleClickedMain();
                break;
            case ABOUT:
                renderAbout();
                handleClickedAbout();
                break;
            case LOADING:
                renderLoading();
                break;
            case LOBBY:
                renderLobby();
                handleClickedLobby();
        }
    }

    private void handleClickedMain() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (about.isClicked()) {
                currentScreen = Screen.ABOUT;
                hasClicked = true;
                return;
            }
            if (join_game.isClicked()) {
                currentScreen = Screen.LOADING;
                connectFunction.accept(null);
                hasClicked = true;
            }
        }
    }

    private void handleClickedAbout() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (go_back.isClicked()) {
                currentScreen = Screen.MAIN;
                hasClicked = true;
                return;
            }
        }
    }


    private void handleClickedLobby() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            // do stuff
        }
    }

    // ****** THESE PREPARE EACH LAYER ******

    private void readyInterfaceLayer() {
        InterfaceTexture title = new InterfaceTexture(ISprite.TITLE);
        join_game = new InterfaceTexture(ISprite.LOBBY);
        instructions = new InterfaceTexture(ISprite.CONTROLS);
        about = new InterfaceTexture(ISprite.ABOUT);

        background.spawn(0,new Vector2f(400f,300f),PHASE,interfaceLayer);
        title.spawn(1,new Vector2f(400f,465f),PHASE,interfaceLayer);
        join_game.spawn(2, new Vector2f(400f, 330f), PHASE, interfaceLayer);
        instructions.spawn(3, new Vector2f(400f, 240f), PHASE, interfaceLayer);
        about.spawn(4, new Vector2f(400f, 150f), PHASE, interfaceLayer);
    }

    private void readyAboutLayer() {
        about_text = new InterfaceTexture(ISprite.ABOUTTEXT);
        go_back = new InterfaceTexture(ISprite.GOBACK);

        about_text.spawn(0, new Vector2f((float) 300.0, (float) 100.0), PHASE, aboutLayer);
        go_back.spawn(1, new Vector2f((float) 300.0, (float) 450.0), PHASE, aboutLayer);
    }

    private void readyLoadingLayer() {
        InterfaceTexture temporary_filler = new InterfaceTexture(ISprite.ORB_P1);
        temporary_filler.spawn(0, new Vector2f((float) 300.0, (float) 100.0), PHASE, loadingLayer);
    }

    public void setupLobby(Sendable sendable) {
        lobbyData = (LobbyData) sendable;
        currentScreen = Screen.LOBBY;
    }


    // ****** THESE RENDER EACH LAYER ******

    private void renderInterface() {
        interfaceLayer.render(PHASE);
    }

    private void renderAbout() {
        aboutLayer.render(PHASE);
    }

    private void renderLoading() {
        loadingLayer.render(PHASE);
    }

    private void renderLobby() {
        int xAlign = 50;
        TextRenderer textRenderer = new TextRenderer();
        textRenderer.drawText("Map: " + lobbyData.getMapID(), xAlign, 540);

        int yCoord = 500;
        textRenderer.drawText("Players: ", xAlign, yCoord);
        for (Integer player : lobbyData.getPlayers()) {
            yCoord = yCoord - 30;
            textRenderer.drawText(player.toString(), xAlign, yCoord);
        }
    }
}
