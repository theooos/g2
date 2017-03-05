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

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL11.*;
import static server.Server.out;

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

    private int oldHealth;
    private double displayHealth;
    private double oldHeat;
    private double displayHeat;

    private GameData gameData;
    private MapRenderer map;
    private Connection conn;
    private boolean fDown;
    private boolean clickDown;
    private boolean eDown;
    private boolean oneDown;
    private boolean twoDown;
    private Draw draw;


    GameRenderer(GameData gd, Connection conn) {
        super();
        this.conn = conn;
        this.gameData = gd;

        fDown = false;
        clickDown = false;
        eDown = false;
        oneDown = false;
        twoDown = false;
        draw = new Draw(width, height);
        oldHealth = 0;
        displayHealth = 100;
        oldHeat = 0;
        displayHeat = 0;


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

            map = new MapRenderer(gd.getMapID());

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
            out(me.getID()+" just switched from "+me.getActiveWeapon());
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            oneDown = true;
        }
        else if (oneDown){
            oneDown = false;
            conn.send(new SwitchObject(me.getID(), true));
            out(me.getID()+" just switched from "+me.getActiveWeapon());
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            twoDown = true;
        }
        else if (twoDown){
            twoDown = false;
            conn.send(new SwitchObject(me.getID(), false));
            out(me.getID()+" just switched from "+me.getActiveWeapon());
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

        drawProjectiles(phase);
        map.renderMap(phase);
        drawOrbs(phase);
        drawPlayers(phase);
        drawHealthBar(p.getHealth(), p.getMaxHealth());
        drawHeatBar(p.getWeaponOutHeat(), p.getActiveWeapon().getMaxHeat());
    }

    private void drawHeatBar(double heat, double maxHeat) {
        float heatBarSensitivity = 0.1f;
        if (heat != oldHeat) {
            oldHeat = heat;
        }

        out("Old heat: "+heat);
        double heatTick = heatBarSensitivity*(oldHeat-displayHeat);

        if (displayHeat > oldHeat && displayHeat+heatTick < oldHeat) {
            displayHeat = oldHeat;
        }
        else if (displayHeat < oldHeat && displayHeat+heatTick > oldHeat) {
            displayHeat = oldHeat;
        } else {
            displayHeat += heatTick;
        }

        if (displayHeat > maxHeat) displayHeat = maxHeat;
        else if (displayHeat < 0) displayHeat = 0;

        out("Max heat: "+maxHeat);
        out("Heat: "+heat);
        out("Display heat: "+displayHeat);
        out("Heat tick: "+heatTick);

        int heatWidth = 10;
        int buffer = 20;
        float maxHeight = height-buffer*2;
        float heatRatio = (float) (displayHeat/maxHeat);
        float green = (204f/255f)*(1-heatRatio);
        out("Heat ratio: "+heatRatio);

        GL11.glColor3f(heatRatio, green, 0f);
        draw.verticalDraw(width - (buffer+heatWidth), buffer+(1-heatRatio)*maxHeight, heatWidth, maxHeight*heatRatio);
    }

    private void drawHealthBar(double health, double maxHealth) {
        float healthBarSensitivity = 0.1f;
        if (health != oldHealth) {
            oldHealth = (int) health;
        }

        double healthTick = healthBarSensitivity*(oldHealth-displayHealth);

        if (displayHealth > oldHealth && displayHealth+healthTick < oldHealth) {
            displayHealth = oldHealth;
        }
        else if (displayHealth < oldHealth && displayHealth+healthTick > oldHealth) {
            displayHealth = oldHealth;
        } else {
            displayHealth += healthTick;
        }

        if (displayHealth > maxHealth) displayHealth = maxHealth;
        else if (displayHealth < 0) displayHealth = 0;
        int healthWidth = 10;
        int buffer = 20;
        float maxHeight = height-buffer*2;
        float healthRatio = (float) (displayHealth/maxHealth);
        float green = (204f/255f)*healthRatio;
        GL11.glColor3f(1-healthRatio, green, 0f);
        draw.verticalDraw(buffer, buffer+(1-healthRatio)*maxHeight, healthWidth, maxHeight*healthRatio);
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

    void setID(int id) {
        this.playerID = id;
    }
}
