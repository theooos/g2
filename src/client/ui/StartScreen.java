package client.ui;

import Audio.GameEffects;
import client.ClientLogic.PlayerInfo;
import client.graphics.*;
import networking.Connection;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by Patrick on 09/03/2017.
 */
public class StartScreen implements Runnable {

    private String WINDOW_TITLE = "PhaseShift";
    static final int SCREEN_HEIGHT = 600;
    static final int SCREEN_WIDTH = 800;

    public static boolean gameRunning = true;

    // Variables to calculate fps
    private long lastFrame;
    private int fps;
    private long lastFPS;

    private TextRenderer rt;
    private static StartScreenRenderer ui;

    public static TextureLoader textureHandler = null;

    public  static Layer interfaceLayer = new Layer();
    public  static Layer aboutLayer = new Layer();
    public  static Layer lobbyLayer = new Layer();

    public boolean gameRender = false;
    private Connection connection ;

    public StartScreen(Connection connection){
       // super();
        this.connection = connection;
    }

    @Override
    public void run()  {
        try {
            GameEffects.init();
            GameEffects.volume = GameEffects.Volume.LOW;
            GameEffects.INTERFACEBACKGROUND.play();

            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.setTitle(WINDOW_TITLE);
            Display.create();

            createOffScreenBuffer();

            GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
            GL11.glClearDepth(1.0f); // Depth Buffer Setup
            GL11.glDisable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDepthMask(false);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            textureHandler = new TextureLoader();
            textureHandler.initialise();

            rt = new TextRenderer();

        } catch (LWJGLException le) {
            System.out.println("Game exiting - exception in initialization:");
            le.printStackTrace();
            StartScreen.gameRunning = false;
            return;
        }

        getDelta();
        lastFPS = getTime();

        ui = new StartScreenRenderer();

        ui.createInterface();
        ui.aboutText();

        int ok = 1;
        boolean notInGame = false;
        boolean notLobby = true;
        boolean destroy = false;

    try {
        while (gameRunning && !Display.isCloseRequested()) {
            int delta = getDelta();
            update(delta);

            if (ok == 1) {
                ui.renderInterface();
            }

            if (ok == 2) {
                ui.renderAbout();
            }

            if (ok == 3) {

                ui.renderLobby();
                notInGame = true;
                int id = PlayerInfo.getId();
                String idPlayer = Integer.toString(id);
                rt.drawText(idPlayer, 100, 100);
            }

            if (notLobby && ui.about.isClicked())
            {
                ok = 2;
                ui.unspawnMenu();
                ui.aboutText();
                ui.renderAbout();
            }

            if(notLobby && ui.goBack.isClicked())
            {
                ok = 1;
                ui.goBack.unSpawn();
                ui.abttext.unSpawn();
                ui.createInterface();
                ui.renderInterface();
            }

            if (notLobby && ui.lobby.isClicked()) {

                notLobby = false;

                ok = 3;
                ui.unspawnForLobby();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ui.createPlay();
                ui.renderLobby();
                if (destroy) {
                    Display.destroy();
                }
            }

            if (notInGame && ui.play.isClicked()) {

                ok = 0;
                String s = "start";
                connection.send(new objects.String(s));
                destroy = true;
                this.setGameRender(true);
                GameEffects.INTERFACEBACKGROUND.stopCurrentClip();
                Display.destroy();
            }

            Display.update();
            Display.sync(60); // cap fps to 60fps
        }
    }
    catch(IllegalStateException de)
        {

        }

        // clean up
        Display.destroy();
    }



    private void update(int delta) {

        updateFPS(); // update FPS Counter
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;
        return delta;
    }

    private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public void setGameRender(boolean game)
    {
        gameRender = game;
    }
    public boolean getGameRender()
    {
        return gameRender;
    }

    public static int SCREEN_TEXTURE_ID = 0;

    private void createOffScreenBuffer() {
        int bytesPerPixel = 3;
        ByteBuffer scratch = ByteBuffer.allocateDirect(1024 * 1024 * bytesPerPixel);
        IntBuffer buf = ByteBuffer.allocateDirect(12).order(ByteOrder.nativeOrder()).asIntBuffer();

        GL11.glGenTextures(buf); // Create Texture In OpenGL
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, buf.get(0));

        SCREEN_TEXTURE_ID = buf.get(0);
        int glType = GL11.GL_RGB;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, glType, 1024, 1024, 0, glType, GL11.GL_UNSIGNED_BYTE, scratch);
    }

}
