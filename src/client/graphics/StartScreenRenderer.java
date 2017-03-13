package client.graphics;

import client.Client;
import client.ClientLogic.GameData;
import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by bianca on 05/03/2017.
 */
public class StartScreenRenderer {

    private enum Screen {MAIN, ABOUT, LOADING, LOBBY}
    private Screen currentScreen = Screen.MAIN;

    private InterfaceTexture play;
    private InterfaceTexture about;
    private InterfaceTexture instructions;
    private InterfaceTexture about_text;
    private InterfaceTexture go_back;
    private InterfaceTexture join_game;

    private static int PHASE = 0;

    private static Layer interfaceLayer = new Layer();
    private static Layer aboutLayer = new Layer();
    private static Layer loadingLayer = new Layer();
    private static Layer lobbyLayer = new Layer();

    private boolean hasClicked = false;

    private Consumer<Void> connectFunction;

    public StartScreenRenderer(Consumer<Void> connectFunction) {
        this.connectFunction = connectFunction;
        readyInterfaceLayer();
        readyAboutLayer();
        readyLoadingLayer();
    }

    public void render() {
        handleClicked();
        switch (currentScreen) {
            case MAIN:
                renderInterface();
                break;
            case ABOUT:
                renderAbout();
                break;
            case LOADING:
                renderLoading();
                break;
            case LOBBY:
                renderLobby();
        }
    }

    private void handleClicked() {
        if(hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if(!hasClicked) {
            if (go_back.isClicked()) {
                currentScreen = Screen.MAIN;
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

    // ****** THESE PREPARE EACH LAYER ******

    private void readyInterfaceLayer() {
        about = new InterfaceTexture(ISprite.ABOUT);
        instructions = new InterfaceTexture(ISprite.HELP);
        join_game = new InterfaceTexture(ISprite.LOBBY);

        about.spawn(1, new Vector2f((float) 300.0, (float) 100.0), PHASE, interfaceLayer);
        instructions.spawn(2, new Vector2f((float) 300.0, (float) 250.0), PHASE, interfaceLayer);
        join_game.spawn(0, new Vector2f((float) 300.0, (float) 400.0), PHASE, interfaceLayer);
    }

    private void readyAboutLayer() {
        about_text = new InterfaceTexture(ISprite.ABOUTTEXT);
        go_back = new InterfaceTexture(ISprite.GOBACK);

        about_text.spawn(0, new Vector2f((float) 300.0, (float) 100.0), PHASE, aboutLayer);
        go_back.spawn(1, new Vector2f((float) 300.0, (float) 450.0), PHASE, aboutLayer);
    }

    private void readyLoadingLayer(){
        InterfaceTexture temporary_filler = new InterfaceTexture(ISprite.ORB_P1);
        temporary_filler.spawn(0,new Vector2f((float) 300.0, (float) 100.0),PHASE,loadingLayer);
    }

    public void readyLobbyLayer(GameData gameData) {

    }



    // ****** THESE RENDER EACH LAYER ******

    private void renderInterface() {
        clearScreen();
        interfaceLayer.render(PHASE);
    }

    private void renderAbout() {
        clearScreen();
        aboutLayer.render(PHASE);
    }

    private void renderLoading(){
        clearScreen();
        loadingLayer.render(PHASE);
    }

    private void renderLobby() {
        clearScreen();
        lobbyLayer.render(PHASE);
    }

    private void clearScreen() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}
