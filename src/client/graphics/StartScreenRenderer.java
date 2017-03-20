package client.graphics;

import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import objects.LobbyData;
import objects.Sendable;
import org.lwjgl.input.Mouse;

import java.util.function.Consumer;

/**
 * Holds all the
 */
public class StartScreenRenderer {

    private enum Screen {MAIN, ABOUT, CONTROLS, LOADING, LOBBY}

    private Screen currentScreen = Screen.MAIN;

    private InterfaceTexture background = new InterfaceTexture(ISprite.BACKGROUND);
    private InterfaceTexture controls;
    private InterfaceTexture about;
    private InterfaceTexture description;
    private InterfaceTexture go_back;
    private InterfaceTexture join_game;

    private static Layer interfaceLayer = new Layer();
    private static Layer controlsLayer = new Layer();
    private static Layer aboutLayer = new Layer();
    private static Layer loadingLayer = new Layer();
    private static Layer lobbyLayer = new Layer();

    private LobbyData lobbyData;

    private boolean hasClicked = false;

    private Consumer<Void> connectFunction;

    public StartScreenRenderer(Consumer<Void> connectFunction) {
        this.connectFunction = connectFunction;
        background.setRatio(0.5f);
        readyInterfaceLayer();
        readyControlsLayer();
        readyAboutLayer();
        readyLoadingLayer();
    }

    public void run() {
        switch (currentScreen) {
            case MAIN:
                renderInterface();
                handleClickedMain();
                break;
            case CONTROLS:
                renderControls();
                handleClickedControls();
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

    // ****** THESE PREPARE EACH LAYER ******

    private void readyInterfaceLayer() {
        InterfaceTexture title = new InterfaceTexture(ISprite.TITLE);
        title.setRatio(0.5f);
        join_game = new InterfaceTexture(ISprite.JOIN_LOBBY_BUTTON);
        controls = new InterfaceTexture(ISprite.CONTROLS_BUTTON);
        about = new InterfaceTexture(ISprite.ABOUT_BUTTON);

        background.spawn(0, 400f, 300f, interfaceLayer);
        title.spawn(1, 400f, 435f, interfaceLayer);
        join_game.spawn(2, 400f, 270f, interfaceLayer);
        controls.spawn(3, 400f, 180f, interfaceLayer);
        about.spawn(4, 400f, 90f, interfaceLayer);
    }

    private void readyControlsLayer(){
        InterfaceTexture controls_guide = new InterfaceTexture(ISprite.CONTROLS_GUIDE);
        controls_guide.setRatio(0.5f);
        go_back = new InterfaceTexture(ISprite.BACK_BUTTON);

        controls_guide.spawn(0, 400f, 300f, controlsLayer);
        go_back.spawn(1, 180f, 550f, controlsLayer);
    }

    private void readyAboutLayer() {
        description = new InterfaceTexture(ISprite.DESCRIPTION);
        description.setRatio(0.5f);
        go_back = new InterfaceTexture(ISprite.BACK_BUTTON);

        description.spawn(0, 400f, 300f, aboutLayer);
        go_back.spawn(1, 640f, 510f, aboutLayer);
    }

    private void readyLoadingLayer() {
        InterfaceTexture loading = new InterfaceTexture(ISprite.LOADING);
        loading.setRatio(0.5f);
        loading.spawn(0, 400f, 300f, loadingLayer);
    }

    public void setupLobby(Sendable sendable) {
        InterfaceTexture lobby = new InterfaceTexture(ISprite.LOBBY);
        lobby.setRatio(0.5f);
        lobby.spawn(0,400f, 300f,lobbyLayer);
        go_back.spawn(1,640f,510f,lobbyLayer);
        lobbyData = (LobbyData) sendable;
        currentScreen = Screen.LOBBY;
    }


    // ****** THESE RENDER EACH LAYER ******

    private void renderInterface() {
        interfaceLayer.render();
    }

    private void renderControls() {
        controlsLayer.render();
    }

    private void renderAbout() {
        aboutLayer.render();
    }

    private void renderLoading() {
        loadingLayer.render();
    }

    private void renderLobby() {
        lobbyLayer.render();
    }


    // ****** BUTTON HANDLERS ******

    private void handleClickedMain() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if(controls.isClicked()){
                currentScreen = Screen.CONTROLS;
                hasClicked = true;
                return;
            }
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

    private void handleClickedControls() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (go_back.isClicked()) {
                currentScreen = Screen.MAIN;
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
            }
        }
    }

    //TODO
    private void handleClickedLobby() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            // do stuff
        }
    }
}
