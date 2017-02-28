package client.graphics;

import client.ClientLogic.GameData;
import networking.Connection;
import objects.FireObject;
import objects.MoveObject;
import objects.PhaseObject;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import server.game.Player;
import server.game.Projectile;
import server.game.Vector2;
import server.game.Zombie;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * Provides the visuals for the game itself.
 */
public class GameRenderer implements Runnable {

    private final String WINDOW_TITLE = "PhaseShift";
    public static boolean gameRunning = true;
    private int width = 800;
    private int height = 600;

    private long lastFrame;
    private int fps;
    private long lastFPS;
    private int playerID;

    private GameData gameData;
    private MapRenderer map;
    private Connection conn;
    private boolean fDown;
    private boolean clickDown;

    int count = 10;

    public GameRenderer(GameData gd, Connection conn) {
        super();
        this.conn = conn;
        this.gameData = gd;

        fDown = false;
        clickDown = false;


        // initialize the window beforehand
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(WINDOW_TITLE);
            Display.create();

            //System.err.print("Creating the display!");

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, width, 0, height, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            map = new MapRenderer(gd.getMapID(), 1);

        } catch (LWJGLException le) {
            System.out.println("Game exiting - exception in initialization:");
            le.printStackTrace();
            GameRenderer.gameRunning = false;
        }
    }

    public void run() {

        getDelta();
        lastFPS = getTime();

        while (GameRenderer.gameRunning && !Display.isCloseRequested()) {

            int delta = getDelta();
            update(delta);
            render();

            Display.update();
            Display.sync(60); // cap fps to 60fps
        }
        // clean up
        Display.destroy();
    }

    private void DrawCircle(float cx, float cy, float r, int num_segments) {
        float theta = (float) (2 * 3.1415926 / (num_segments));
        float tangetial_factor = (float) Math.tan(theta);//calculate the tangential factor
        float radial_factor = (float) Math.cos(theta);//calculate the radial factor

        float x = r;//we start at angle = 0
        float y = 0;

        GL11.glBegin(GL_TRIANGLE_FAN);

        for (int ii = 0; ii < num_segments; ii++) {
            glVertex2f(x + cx, y + cy);//output vertex

            //calculate the tangential vector; remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them
            float tx = -y;
            float ty = x;

            //add the tangential vector
            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            //correct using the radial factor
            x *= radial_factor;
            y *= radial_factor;
        }
        GL11.glEnd();
    }

    private Vector2 getDirFromMouse(Vector2 pos) {
        Vector2 mousePos = new Vector2(Mouse.getX(), Mouse.getY());
        Vector2 dir = pos.vectorTowards(mousePos);
        return dir.normalise();
    }


    private void positionBullet(Vector2 pos, Vector2 dir) {
        Vector2 cursor = pos.add(dir.mult(21));
        float lastX = cursor.getX();
        float lastY = cursor.getY();
        //Mouse.setGrabbed(true);

        if (lastX > 0 && lastY > 0)
            DrawCircle(lastX, lastY, 10, 50);
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void update(int delta) {

        Player me = gameData.getPlayer(playerID);
        Vector2 pos = me.getPos();
        float xPos = pos.getX();
        float yPos = pos.getY();

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) xPos -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) xPos += 0.35f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) yPos -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) yPos += 0.35f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            fDown = true;
        }
        else if (fDown){
            fDown = false;
            conn.send(new PhaseObject(me.getID()));
        }

        if (Mouse.isButtonDown(0)) {
            if (!clickDown) {
                conn.send(new FireObject(me.getID(), true));
                clickDown = true;
            }
        }
        else if (clickDown){
            conn.send(new FireObject(me.getID(), false));
            clickDown = false;
        }


        // keep quad on the screen
        if (xPos < 0) xPos = 0;
        if (xPos > 800) xPos = 800;
        if (yPos < 0) yPos = 0;
        if (yPos > 600) yPos = 600;

        if(pos.getX() != xPos || pos.getY() != yPos) {
            me.setPos(new Vector2(xPos, yPos));
            gameData.updatePlayer(me);
            //System.err.println("Old: "+pos+" New: ("+xPos+", "+yPos+ ") Me: "+me.getPos());
            conn.send(new MoveObject(me.getPos(), me.getDir(), playerID));
        }

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

    private void render() {
        // Clear the screen and depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        map.renderMap();
        int phase = gameData.getPlayer(playerID).getPhase();
        drawProjectiles(phase);
        drawZombies(phase);
        drawPlayers(phase);

    }

    private void drawPlayers(int phase) {
        HashMap<Integer, Player> players = gameData.getPlayers();
        int radius = players.get(0).getRadius();
        for (Player p : players.values()) {
            if (p.getPhase() == phase) {
                if (p.getTeam() == 0) {
                    GL11.glColor3f(1, 0.33f, 0.26f);
                } else {
                    GL11.glColor3f(0.2f, 0.9f, 0.5f);
                }
                DrawCircle(p.getPos().getX(), height - p.getPos().getY(), radius, 100);

                if (p.getID() != playerID) {
                    positionBullet(new Vector2(p.getPos().getX(), height - p.getPos().getY()), p.getDir());
                }
                else {
                    Vector2 pos = new Vector2(p.getPos().getX(), height - p.getPos().getY());
                    Vector2 dir = getDirFromMouse(pos);
                    positionBullet(pos, dir);
                    p.setDir(dir);
                    conn.send(new MoveObject(p.getPos(), p.getDir(), playerID));
                }
            }
        }
    }

    private void drawZombies(int phase) {
        HashMap<Integer, Zombie> zombies= gameData.getZombies();
        GL11.glColor3f(0.2f, 0.2f, 1f);
        for (Zombie z: zombies.values()) {
            if (phase == z.getPhase()) {
                DrawCircle(z.getPos().getX(), height - z.getPos().getY(), z.getRadius(), 100);
            }
        }
    }

    private void drawProjectiles(int phase) {
        HashMap<Integer, Projectile> projectiles = gameData.getProjectiles();
        for (Projectile p : projectiles.values()) {
            //System.out.println("showing projectile");
            if (phase == p.getPhase()) {
                if (p.getTeam() == 0) {
                    GL11.glColor3f(1f, 0.1f, 0.1f);
                } else {
                    GL11.glColor3f(0.1f, 1f, 0.1f);
                }
                DrawCircle(p.getPos().getX(), p.getPos().getY(), p.getRadius(), 100);
            }
        }
    }

    private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    void setID(int id) {
        this.playerID = id;
    }
}
