package client.graphics;

import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by bianca on 05/03/2017.
 */
public class StartScreenRenderer {

    private enum Screen {MAIN,ABOUT,LOBBY}

    private InterfaceTexture play;
    private InterfaceTexture about;
    private InterfaceTexture instructions;
    private InterfaceTexture aboutText;
    private InterfaceTexture goBack;
    private InterfaceTexture lobby;

    private Screen currentScreen = Screen.MAIN;
    private static int PHASE = 0;

    private static Layer interfaceLayer = new Layer();
    private static Layer aboutLayer = new Layer();
    private static Layer lobbyLayer = new Layer();

    public StartScreenRenderer(){
        createInterface();
        aboutText();
    }

    public void render(){

        if(goBack.isClicked()) currentScreen = Screen.MAIN;
        if(about.isClicked()) currentScreen = Screen.ABOUT;
        if(lobby.isClicked()) currentScreen = Screen.LOBBY;

        switch(currentScreen){
            case MAIN:
                renderInterface();
                break;
            case ABOUT:
                renderAbout();
                break;
            case LOBBY:
                renderLobby();
        }
    }

    private void unSpawnAll(){
        unspawnMenu();
        goBack.unSpawn();
        aboutText.unSpawn();
    }

    public void createInterface()
    {
        //play = new ButtonTexture(ISprite.START);
        about = new InterfaceTexture(ISprite.ABOUT);
        instructions = new InterfaceTexture(ISprite.HELP);
        lobby = new InterfaceTexture(ISprite.LOBBY);

        //play.spawn(0,GameRenderer.correctCoordinates(new Vector2f((float)300.0,(float)100.0)),PHASE,GameRenderer.mainUI);
        lobby.spawn(0, new Vector2f((float) 300.0, (float) 100.0), PHASE, interfaceLayer);
        about.spawn(1, new Vector2f((float)300.0,(float)250.0),PHASE, interfaceLayer);
        instructions.spawn(2,new Vector2f((float)300.0,(float)400.0),PHASE, interfaceLayer);
    }

    public void createPlay()
    {
        play = new InterfaceTexture(ISprite.START);
        play.spawn(0, new Vector2f((float)300.0,(float)100.0),PHASE, lobbyLayer);
    }

    public void aboutText()
    {
        aboutText = new InterfaceTexture(ISprite.ABOUTTEXT);
        aboutText.spawn(0, new Vector2f((float)300.0,(float)100.0),PHASE, aboutLayer);

        goBack = new InterfaceTexture(ISprite.GOBACK);
        goBack.spawn(1, new Vector2f((float)300.0,(float)450.0),PHASE, aboutLayer);
    }

    public void unspawnMenu()
    {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        lobby.unSpawn();
        instructions.unSpawn();
        about.unSpawn();
    }

    public void unspawnLobby()
    {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        play.unSpawn();
    }

    public void unspawnForLobby()
    {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        instructions.unSpawn();
        about.unSpawn();
    }

    public void renderInterface()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        //GameRenderer.mainUI.render(PHASE);
        interfaceLayer.render(PHASE);
    }

    public void renderAbout()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        //GameRenderer.aboutUI.render(PHASE);
        aboutLayer.render(PHASE);
    }

    public void renderLobby()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        //Layer startButton = startm.getStartButton();
        //startButton.render();
        lobbyLayer.render(PHASE);
    }
}
