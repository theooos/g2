package client.graphics;

import client.ClientLogic.GameData;
import networking.Connection;
import objects.FireObject;
import objects.MoveObject;
import objects.PhaseObject;
import objects.SwitchObject;
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
import server.game.Orb;

import java.security.Key;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDepthMask;

/**
 * Provides the visuals for the game itself.
 */
public class GameRenderer implements Runnable {

    private final String WINDOW_TITLE = "PhaseShift";
    private static boolean gameRunning = true;
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
    private boolean eDown;
    private boolean oneDown;
    private boolean twoDown;
    private boolean tabPressed;

    private Draw draw;
    private Pulse pulse;


    GameRenderer(GameData gd, Connection conn, int playerID) {
        super();
        this.conn = conn;
        this.gameData = gd;
        this.playerID = playerID;

        fDown = false;
        clickDown = false;
        eDown = false;
        oneDown = false;
        twoDown = false;
        tabPressed = false;
        draw = new Draw(width, height);

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
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            map = new MapRenderer(gd.getMapID());
            Player me = gameData.getPlayer(playerID);
            if (me.getPhase() == 0) {
                //blue phase
                pulse = new Pulse(me.getPos(), me.getRadius(), 0, 0, 1, height, width, 20, 20, 0);
            }
            else {
                //red phase
                pulse = new Pulse(me.getPos(), me.getRadius(), 1, 0, 0, height, width, 20, 20, 1);
            }

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

    private Vector2 getDirFromMouse(Vector2 pos) {
        Vector2 mousePos = new Vector2(Mouse.getX(), Mouse.getY());
        Vector2 dir = pos.vectorTowards(mousePos);
        dir = dir.normalise();
        return new Vector2(dir.getX(), 0-dir.getY());
    }


    private void positionBullet(Vector2 pos, Vector2 dir) {
        Vector2 cursor = pos.add((new Vector2(dir.getX(), 0-dir.getY())).mult(21));
        float lastX = cursor.getX();
        float lastY = cursor.getY();
        //Mouse.setGrabbed(true);

        if (lastX > 0 && lastY > 0)
            draw.drawCircle(lastX, lastY, 10, 50);
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
            if (me.getPhase() == 1) {
                //switch to blue phase
                pulse = new Pulse(me.getPos(), me.getRadius(), 0, 0, 1, height, width, 20, 20, 0);
            }
            else {
                //switch to red phase
                pulse = new Pulse(me.getPos(), me.getRadius(), 1, 0, 0, height, width, 20, 20, 1);
            }

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            eDown = true;
        }
        else if (eDown){
            eDown = false;
            if (me.isWeaponOneOut()) {
                conn.send(new SwitchObject(me.getID(), false));
            }
            else {
                conn.send(new SwitchObject(me.getID(), true));
            }
        }

        tabPressed = Keyboard.isKeyDown(Keyboard.KEY_TAB);

        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            oneDown = true;
        }
        else if (oneDown){
            oneDown = false;
            conn.send(new SwitchObject(me.getID(), true));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            twoDown = true;
        }
        else if (twoDown){
            twoDown = false;
            conn.send(new SwitchObject(me.getID(), false));
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

        Player p = gameData.getPlayer(playerID);
        int phase = p.getPhase();

        if (pulse.isAlive()) {
            drawStencil();
        }
        else {
            drawProjectiles(phase);
            map.renderMap(phase);
            drawOrbs(phase);
            drawPlayers(phase);
        }
        draw.drawHealthBar(p.getHealth(), p.getMaxHealth());
        draw.drawHeatBar(p.getWeaponOutHeat(), p.getActiveWeapon().getMaxHeat());

        if (tabPressed) {
            draw.shadeScreen();
        }
    }

    private void drawStencil() {
        int newPhase = pulse.getNewPhase();
        int oldPhase = 1;
        if (newPhase == 1) oldPhase = 0;

        drawProjectiles(oldPhase);
        map.renderMap(oldPhase);
        drawOrbs(oldPhase);
        drawPlayers(oldPhase);

        GL11.glEnable(GL11.GL_STENCIL_TEST);

        glColorMask(false,false,false,false);
        glStencilFunc(GL_ALWAYS, 1, 0xFF); // Set any stencil to 1
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        glStencilMask(0xFF); // Write to stencil buffer
        glDepthMask(false); // Don't write to depth buffer
        glClear(GL_STENCIL_BUFFER_BIT); // Clear stencil buffer (0 by default)

        draw.drawCircle(pulse.getStart().getX(), height-pulse.getStart().getY(), pulse.getRadius(), 500);

        glStencilFunc(GL_EQUAL, 1, 0xFF); // Pass test if stencil value is 1
        glStencilMask(0x00); // Don't write anything to stencil buffer
        glDepthMask(true); // Write to depth buffer
        glColorMask(true,true,true,true);

        //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glColor3f(0, 0, 0);
        draw.drawCircle(pulse.getStart().getX(), height-pulse.getStart().getY(), pulse.getRadius(), 500);
        drawProjectiles(newPhase);
        map.renderMap(newPhase);
        drawOrbs(newPhase);
        drawPlayers(newPhase);

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        pulse.draw();

    }

    private void drawPlayers(int phase) {
        ConcurrentHashMap<Integer, Player> players = gameData.getPlayers();
        int radius = players.get(0).getRadius();

        for (Player p : players.values()) {
            if (p.getPhase() == phase) {
                if (p.getTeam() == 0) {
                    GL11.glColor3f(1, 0.33f, 0.26f);
                } else {
                    GL11.glColor3f(0.2f, 0.9f, 0.5f);
                }
                draw.drawCircle(p.getPos().getX(), height - p.getPos().getY(), radius, 100);

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

    private void drawOrbs(int phase) {
        HashMap<Integer, Orb> orbs = gameData.getOrbs();
        GL11.glColor3f(0.2f, 0.2f, 1f);
        for (Orb o: orbs.values()) {
            if (phase == o.getPhase()) {
                draw.drawCircle(o.getPos().getX(), height - o.getPos().getY(), o.getRadius(), 100);
            }
        }
    }

    private void drawProjectiles(int phase) {
        ConcurrentHashMap<Integer, Projectile> projectiles = gameData.getProjectiles();
        for (Projectile p : projectiles.values()) {
            if (phase == p.getPhase()) {
                if (p.getTeam() == 0) {
                    GL11.glColor3f(1f, 0.1f, 0.1f);
                } else {
                    GL11.glColor3f(0.1f, 1f, 0.1f);
                }
                draw.drawCircle(p.getPos().getX(), height - p.getPos().getY(), p.getRadius(), 100);
            }
        }
    }

    private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

}
