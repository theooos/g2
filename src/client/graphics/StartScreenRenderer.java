package client.graphics;

import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import client.ui.StartScreen;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by bianca on 05/03/2017.
 */
public class StartScreenRenderer {

    public InterfaceTexture play;
    public InterfaceTexture about;
    public InterfaceTexture instructions;
    public InterfaceTexture abttext;
    public InterfaceTexture goBack;
    public InterfaceTexture lobby;

    public static int PHASE = 0;

    public void createInterface()
    {
        //play = new ButtonTexture(ISprite.START);
        about = new InterfaceTexture(ISprite.ABOUT);
        instructions = new InterfaceTexture(ISprite.HELP);
        lobby = new InterfaceTexture(ISprite.LOBBY);

        //play.spawn(0,GameRenderer.correctCoordinates(new Vector2f((float)300.0,(float)100.0)),PHASE,GameRenderer.mainUI);
        lobby.spawn(0, new Vector2f((float) 300.0, (float) 100.0), PHASE, StartScreen.interfaceLayer);
        about.spawn(1, new Vector2f((float)300.0,(float)250.0),PHASE, StartScreen.interfaceLayer);
        instructions.spawn(2,new Vector2f((float)300.0,(float)400.0),PHASE, StartScreen.interfaceLayer);
    }

    public void createPlay()
    {
        play = new InterfaceTexture(ISprite.START);
        play.spawn(0, new Vector2f((float)300.0,(float)100.0),PHASE, StartScreen.lobbyLayer);
    }

    public void aboutText()
    {
        abttext = new InterfaceTexture(ISprite.ABOUTTEXT);
        abttext.spawn(0, new Vector2f((float)300.0,(float)100.0),PHASE, StartScreen.aboutLayer);

        goBack = new InterfaceTexture(ISprite.GOBACK);
        goBack.spawn(1, new Vector2f((float)300.0,(float)450.0),PHASE, StartScreen.aboutLayer);
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
        StartScreen.interfaceLayer.render(PHASE);
    }

    public void renderAbout()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        //GameRenderer.aboutUI.render(PHASE);
        StartScreen.aboutLayer.render(PHASE);
    }

    public void renderLobby()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        //Layer startButton = startm.getStartButton();
        //startButton.render();
        StartScreen.lobbyLayer.render(PHASE);
    }
}
