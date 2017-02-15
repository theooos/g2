package client.graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * Created by bianca on 14/02/2017.
 */
public class GameRenderer {

    private String WINDOW_TITLE = "PhaseShift";
    public static boolean gameRunning = true;
    private int width = 800;
    private int height = 600;

    private float xPos = 100;
    private float yPos = 200;
    //private float rotation = 0;

    private long lastFrame;
    private int fps;
    private long lastFPS;

    private MapRenderer map;


    public GameRenderer() {
        // initialize the window beforehand
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(WINDOW_TITLE);
            Display.create();

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, width, 0, height, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            map = new MapRenderer(0, 1);

        } catch (LWJGLException le) {
            System.out.println("Game exiting - exception in initialization:");
            le.printStackTrace();
            GameRenderer.gameRunning = false;
            return;
        }
    }


    public void execute() {

        getDelta();
        lastFPS = getTime();

        while (GameRenderer.gameRunning && !Display.isCloseRequested()) {

            int delta = getDelta();
            update(delta);

            render();
            //keyboardInput();

            Display.update();
            Display.sync(60); // cap fps to 60fps

        }
        // clean up
        Display.destroy();
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void update(int delta) {
        // rotate quad
        //rotation += 0.15f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) xPos -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) xPos += 0.35f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) yPos += 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) yPos -= 0.35f * delta;

        // keep quad on the screen
        if (xPos < 0) xPos = 0;
        if (xPos > 800) xPos = 800;
        if (yPos < 0) yPos = 0;
        if (yPos > 600) yPos = 600;

        updateFPS(); // update FPS Counter
    }

    private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    private void render(){
        // Clear the screen and depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // set the color of the quad (R,G,B,A)
        //GL11.glColor3f(0.5f,0.5f,1.0f);

        // rotate quad
        //GL11.glPushMatrix();
        //GL11.glTranslatef(xPos, yPos, 0);
        //GL11.glRotatef(rotation, 0f, 0f, 1f);
        //GL11.glTranslatef(-xPos, -yPos, 0);

        //map.renderMap();

        /*GL11.glColor3f(0.5f,0.5f,1.0f);
        GL11.glColor3f(245.0f, 225.0f, 65.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0 , 0 );
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 10);
        GL11.glVertex2f(width, 0);
        GL11.glEnd();*/

        // update movement
        GL11.glColor3f(0.5f,0.5f,1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(xPos - 50, yPos - 50);
        GL11.glVertex2f(xPos + 50, yPos - 50);
        GL11.glVertex2f(xPos + 50, yPos + 50);
        GL11.glVertex2f(xPos - 50, yPos + 50);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public void keyboardInput() {

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                    System.out.println("A Key Pressed");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                    System.out.println("S Key Pressed");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                    System.out.println("D Key Pressed");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_W) {
                    System.out.println("W Key Pressed");
                }
            } else {
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                    System.out.println("A Key Released");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                    System.out.println("S Key Released");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                    System.out.println("D Key Released");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_W) {
                    System.out.println("W Key Released");
                }
            }
        }
    }

    private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    public static void main(String argv[]) {
        new GameRenderer ().execute();
        System.exit(0);
    }
}
