package client;

import Audio.GameEffects;
import client.graphics.StartScreenRenderer;
import client.graphics.TextRenderer;
import client.graphics.TextureLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Client {

    public enum Mode {SPLASH, GAME}

    private static final String WINDOW_TITLE = "PhaseShift";
    private static final int SCREEN_HEIGHT = 600;
    private static final int SCREEN_WIDTH = 800;
    private static int SCREEN_TEXTURE_ID = 0;
    private static final boolean FULLSCREEN = false;

    private boolean running = true;
    private Mode currentMode = Mode.SPLASH;

    public static TextureLoader textureLoader;
    private TextRenderer textRenderer;

    private StartScreenRenderer startScreen;

    private Client() {
        initialise();
        loop();
    }

    private void loop() {
        while(!Display.isCloseRequested() && running){
            // clear screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            startScreen.createInterface();
            startScreen.aboutText();
            // let subsystem paint
            switch (currentMode){
                case SPLASH:
                    startScreen.render();
                    break;
                case GAME:
                    break;
                default:
                    System.out.println("Not in a mode.");
            }

            // update window contents
            Display.update();
            Display.sync(60);
        }
    }

    private void initialise() {
        // initialize the window beforehand
        try {
            setDisplayMode();
            Display.setTitle(WINDOW_TITLE);
            Display.setFullscreen(FULLSCREEN);
            Display.create();

            // enable textures since we're going to use these for our sprites
            glEnable(GL_TEXTURE_2D);

            // disable the OpenGL depth test since we're rendering 2D graphics
            glDisable(GL_DEPTH_TEST);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();

            glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            textureLoader = new TextureLoader();
            textureLoader.initialise();

            textRenderer = new TextRenderer();

            startScreen = new StartScreenRenderer();

            GameEffects.init();
            GameEffects.volume = GameEffects.Volume.LOW;
            GameEffects.MUSIC.play();

        } catch (LWJGLException le) {
            System.out.println("Game exiting - exception in initialization:");
            le.printStackTrace();
            running = false;
        }
    }

    private boolean setDisplayMode() {
        try {
            // get modes
            DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(SCREEN_WIDTH, SCREEN_HEIGHT, -1, -1, -1, -1, 60, 60);

            org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
                    "width=" + SCREEN_WIDTH,
                    "height=" + SCREEN_HEIGHT,
                    "freq=" + 60,
                    "bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to enter fullscreen, continuing in windowed mode");
        }

        return false;
    }

    public static void main(String argv[]) {
        new Client();
        System.exit(0);
    }
}
