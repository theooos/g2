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

    public enum Screen {MAIN, ABOUT, CONTROLS, LOADING, LOBBY}

    private Screen currentScreen = Screen.MAIN;

    private InterfaceTexture background = new InterfaceTexture(ISprite.BACKGROUND);
    private InterfaceTexture controls_button = new InterfaceTexture(ISprite.CONTROLS_BUTTON);
    private InterfaceTexture about_button = new InterfaceTexture(ISprite.ABOUT_BUTTON);
    private InterfaceTexture description = new InterfaceTexture(ISprite.ABOUT_SCREEN);
    private InterfaceTexture back_button = new InterfaceTexture(ISprite.BACK_BUTTON);
    private InterfaceTexture join_lobby_button = new InterfaceTexture(ISprite.JOIN_LOBBY_BUTTON);

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
        description.setRatio(0.5f);
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

        background.spawn(0, 400f, 300f, interfaceLayer);
        title.spawn(1, 400f, 435f, interfaceLayer);
        join_lobby_button.spawn(2, 400f, 270f, interfaceLayer);
        controls_button.spawn(3, 400f, 180f, interfaceLayer);
        about_button.spawn(4, 400f, 90f, interfaceLayer);
    }

    private void readyControlsLayer(){
        InterfaceTexture controls_guide = new InterfaceTexture(ISprite.CONTROLS_SCREEN);
        controls_guide.setRatio(0.5f);

        controls_guide.spawn(0, 400f, 300f, controlsLayer);
        back_button.spawn(1, 180f, 550f, controlsLayer);
    }

    private void readyAboutLayer() {
        description.spawn(0, 400f, 300f, aboutLayer);
        back_button.spawn(1, 640f, 510f, aboutLayer);
    }

    private void readyLoadingLayer() {
        InterfaceTexture loading = new InterfaceTexture(ISprite.LOADING_SCREEN);
        loading.setRatio(0.5f);
        loading.spawn(0, 400f, 300f, loadingLayer);
    }

    public void setupLobby(Sendable sendable) {
        lobbyData = (LobbyData) sendable;
        InterfaceTexture lobby = new InterfaceTexture(ISprite.LOBBY_SCREEN);
        lobby.setRatio(0.5f);
        lobby.spawn(0,400f, 300f,lobbyLayer);
        back_button.spawn(1,640f,510f,lobbyLayer);
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
            if(controls_button.isClicked()){
                currentScreen = Screen.CONTROLS;
                hasClicked = true;
            }
            else if (about_button.isClicked()) {
                currentScreen = Screen.ABOUT;
                hasClicked = true;
            }
            else if (join_lobby_button.isClicked()) {
                currentScreen = Screen.LOADING;
                connectFunction.accept(null);
                hasClicked = true;
            }
        }
    }

    private void handleClickedControls() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (back_button.isClicked()) {
                currentScreen = Screen.MAIN;
                hasClicked = true;
            }
        }
    }

    private void handleClickedAbout() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (back_button.isClicked()) {
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

    public void setCurrentScreen(Screen screen){
        currentScreen = screen;
    }
}
