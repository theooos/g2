package client.graphics;

import client.ClientSettings;
import client.graphics.Sprites.BackgroundTexture;
import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import objects.GameData;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

/**
 * Renders the in-game menus/scoreboard.
 */
class InGameMenuRenderer {

    private GameData gameData;
    private int playerID;

    private BackgroundTexture background = new BackgroundTexture(ISprite.OPTIONS);
    private InterfaceTexture resume;
    private InterfaceTexture exit;
    private InterfaceTexture mute;

    private static int PHASE = 0;

    private static Layer options = new Layer();


    InGameMenuRenderer(GameData gameData, int playerID) {
        this.gameData = gameData;
        this.playerID = playerID;
        readyMenuLayer();
    }

    void readyMenuLayer(){
        resume = new InterfaceTexture(ISprite.CONTINUE);
        exit = new InterfaceTexture(ISprite.ENDGAME);
        mute = new InterfaceTexture(ISprite.MUTE);

        background.spawn(0,new Vector2f(400f,300f),PHASE,options);
        resume.spawn(2, new Vector2f(400f, 270f), PHASE, options);
        exit.spawn(3, new Vector2f(400f, 180f), PHASE, options);
        mute.spawn(4, new Vector2f(400f, 90f), PHASE, options);
    }

    void handleClickedMenu(){

    }

    void handleClickedScoreboard(){

    }

    void handleClickedEndScreen(){

    }

    void renderMenu(){
       enableTextureScreen();
       options.render(PHASE);
    }

    void renderScoreboard(){
        TextRenderer textRenderer = new TextRenderer();
        textRenderer.drawText("The scoreboard will be here.", 0, 50);
    }

    void renderEndScreen(){
        TextRenderer textRenderer = new TextRenderer();
        textRenderer.drawText("The end game screen will be here.", 0, 50);
    }

    private void enableTextureScreen() {
        GL11.glDisable(GL11.GL_COLOR);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glDisable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, ClientSettings.SCREEN_WIDTH, 0, ClientSettings.SCREEN_HEIGHT, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
