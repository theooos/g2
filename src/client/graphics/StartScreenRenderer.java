package client.graphics;

import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import objects.LobbyData;
import objects.Sendable;
import org.lwjgl.input.Mouse;
import server.game.Map;

import java.util.function.Consumer;

import static client.graphics.GameManager.out;

/**
 * Holds all the
 */
public class StartScreenRenderer {

    public enum Screen {MAIN, ABOUT, CONTROLS, SETTINGS, LOADING, LOBBY}

    private Screen currentScreen = Screen.MAIN;

    private InterfaceTexture background = new InterfaceTexture(ISprite.BACKGROUND);
    private InterfaceTexture controls_button = new InterfaceTexture(ISprite.CONTROLS_BUTTON);
    private InterfaceTexture about_button = new InterfaceTexture(ISprite.ABOUT_BUTTON);
    private InterfaceTexture description = new InterfaceTexture(ISprite.ABOUT_SCREEN);
    private InterfaceTexture back_button = new InterfaceTexture(ISprite.BACK_BUTTON);
    private InterfaceTexture back_button_half = new InterfaceTexture(ISprite.BACK_BUTTON_HALF);
    private InterfaceTexture solo_game_button = new InterfaceTexture(ISprite.SOLO_GAME_BUTTON);
    private InterfaceTexture versus_game_button = new InterfaceTexture(ISprite.VERSUS_GAME_BUTTON);
    private InterfaceTexture settings_button = new InterfaceTexture(ISprite.SETTINGS_BUTTON);

    private static Layer interfaceLayer = new Layer();
    private static Layer controlsLayer = new Layer();
    private static Layer aboutLayer = new Layer();
    private static Layer loadingLayer = new Layer();
    private static Layer lobbyLayer = new Layer();

    private LobbyData lobbyData;

    private boolean hasClicked = false;

    private Consumer<Void> connectFunction;
    private int playerID;

    public StartScreenRenderer(Consumer<Void> connectFunction, int playerID) {
        this.connectFunction = connectFunction;
        background.setRatio(0.5f);
        description.setRatio(0.5f);
        this.playerID = playerID;

        readyInterfaceLayer();
        readyControlsLayer();
        readyAboutLayer();
        readyLoadingLayer();
    }

    public void run() {
        switch (currentScreen) {
            case MAIN:
                interfaceLayer.render();
                handleClickedMain();
                break;
            case CONTROLS:
                controlsLayer.render();
                handleClickedControls();
                break;
            case ABOUT:
                aboutLayer.render();
                handleClickedAbout();
                break;
            case SETTINGS:
                SettingsRenderer.run(e -> currentScreen = Screen.MAIN);
                break;
            case LOADING:
                loadingLayer.render();
                break;
            case LOBBY:
                TextRenderer smallText = new TextRenderer(20);
                lobbyLayer.render();
                try {
                    Draw.drawLobby(lobbyData, new Map(lobbyData.getMapID()), playerID, smallText);
                    out("Drawing lobby");
                }
                catch (Exception e) {
                    System.err.println("Can't load map from lobby");
                }
                handleClickedLobby();

        }
    }

    // ****** THESE PREPARE EACH LAYER ******

    private void readyInterfaceLayer() {
        InterfaceTexture title = new InterfaceTexture(ISprite.TITLE);
        title.setRatio(0.5f);

        background.spawn(0, 400f, 300f, interfaceLayer);
        title.spawn(1, 400f, 435f, interfaceLayer);
        solo_game_button.spawn(2, 220f, 270f, interfaceLayer);
        versus_game_button.spawn(3, 580f,270f,interfaceLayer);
        controls_button.spawn(4, 220f, 180f, interfaceLayer);
        about_button.spawn(5, 580f, 180f, interfaceLayer);
        settings_button.spawn(6,400f,90f,interfaceLayer);
    }

    private void readyControlsLayer(){
        InterfaceTexture controls_guide = new InterfaceTexture(ISprite.CONTROLS_SCREEN);
        controls_guide.setRatio(0.5f);

        controls_guide.spawn(0, 400f, 300f, controlsLayer);
        back_button_half.spawn(1, 160f, 90f, controlsLayer);
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
            else if (solo_game_button.isClicked()) {
                currentScreen = Screen.LOADING;
                connectFunction.accept(null);
                hasClicked = true;
            } else if (versus_game_button.isClicked()) {
                currentScreen = Screen.LOADING;
                connectFunction.accept(null);
                hasClicked = true;
            }
            else if(settings_button.isClicked()){
                currentScreen = Screen.SETTINGS;
                hasClicked = true;
            }
        }
    }

    private void handleClickedControls() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (back_button_half.isClicked()) {
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
