package client.graphics;

import client.audio.GameEffects;
import client.logic.GameData;
import networking.Connection;
import objects.FireObject;
import objects.MoveObject;
import objects.PhaseObject;
import objects.SwitchObject;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDepthMask;

/**
 * Provides the visuals for the game itself.
 */
public class GameRenderer {

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
    private CollisionManager collisions;

    private boolean fDown;
    private boolean clickDown;
    private boolean eDown;
    private boolean oneDown;
    private boolean twoDown;
    private boolean tabPressed;

    private Draw draw;
    private Pulse pulse;

    private boolean displayCollisions;
    private float rotation;

    public GameRenderer(GameData gd, Connection conn, int playerID) {
        super();
        this.conn = conn;
        this.gameData = gd;
        this.playerID = playerID;

        rotation = 0;
        fDown = false;
        clickDown = false;
        eDown = false;
        oneDown = false;
        twoDown = false;
        tabPressed = false;

        draw = new Draw(width, height);
        collisions = new CollisionManager(gd);
        displayCollisions = false;


        map = new MapRenderer(gd.getMapID());
        Player me = gameData.getPlayer(playerID);
        pulse = new Pulse(me.getPos(), me.getRadius(), me.getPhase(), 0, 1 - me.getPhase(), height, width, 20, 20, me.getPhase(), true);
    }

    private Vector2 getDirFromMouse(Vector2 pos) {
        Vector2 mousePos = new Vector2(Mouse.getX(), Mouse.getY());
        Vector2 dir = pos.vectorTowards(mousePos);
        dir = dir.normalise();
        return new Vector2(dir.getX(), 0 - dir.getY());
    }


    private void positionBullet(Vector2 pos, Vector2 dir) {
        Vector2 cursor = pos.add((new Vector2(dir.getX(), 0 - dir.getY())).mult(21));
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

        rotation += 0.15f * delta;
//        rotation %= rotation%Math.PI;

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
            GameEffects.GAMEMUSIC.pause(1);
            GameEffects.PHASE.play();
        } else if (fDown) {
            fDown = false;
            int newPhase = 0;
            if (me.getPhase() == 0) {
                newPhase = 1;
            }
            Player p = new Player(me);
            p.setPhase(newPhase);
            if (collisions.validPosition(p)) {
                conn.send(new PhaseObject(me.getID()));
                pulse = new Pulse(me.getPos(), me.getRadius(), newPhase, 0, 1 - newPhase, height, width, 20, 20, newPhase, true);
            } else {
                //invalid phase
                pulse = new Pulse(me.getPos(), me.getRadius(), 0.3f, 0.3f, 0.3f, height, width, 20, 20, me.getPhase(), 250, false);
            }

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            eDown = true;
        } else if (eDown) {
            eDown = false;
            if (me.isWeaponOneOut()) {
                conn.send(new SwitchObject(me.getID(), false));
            } else {
                conn.send(new SwitchObject(me.getID(), true));
            }
        }

        tabPressed = Keyboard.isKeyDown(Keyboard.KEY_TAB);

        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            oneDown = true;
        } else if (oneDown) {
            oneDown = false;
            conn.send(new SwitchObject(me.getID(), true));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            twoDown = true;
        } else if (twoDown) {
            twoDown = false;
            conn.send(new SwitchObject(me.getID(), false));
            displayCollisions = !displayCollisions;
        }

        if (Mouse.isButtonDown(0)) {
            if (!clickDown) {
                conn.send(new FireObject(me.getID(), true));
                clickDown = true;
            }
        } else if (clickDown) {
            GameEffects.SHOOT.play();
            conn.send(new FireObject(me.getID(), false));
            clickDown = false;
        }


        // keep quad on the screen
        if (xPos < 0) xPos = 0;
        if (xPos > 800) xPos = 800;
        if (yPos < 0) yPos = 0;
        if (yPos > 600) yPos = 600;

        if (pos.getX() != xPos || pos.getY() != yPos) {
            me.setPos(new Vector2(xPos, yPos));
            if (collisions.validPosition(me)) {
                gameData.updatePlayer(me);
                conn.send(new MoveObject(me.getPos(), me.getDir(), playerID, me.getMoveCount()));
            } else {
                me.setPos(pos);
            }
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

    public void render() {
        update(getDelta());
        System.out.println(gameData);

        // Clear the screen and depth buffer
//        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        Player p = gameData.getPlayer(playerID);
        int phase = p.getPhase();

        if (pulse.isAlive() && pulse.isShowOtherPhase()) {
            drawStencil();
        } else {
            drawProjectiles(phase);
            map.renderMap(phase);
            drawOrbs(phase);
            drawPlayers(phase);
            drawPowerUps(phase);
            if (pulse.isAlive()) {
                pulse.draw();
            }
        }
        draw.drawHealthBar(p.getHealth(), p.getMaxHealth());
        draw.drawHeatBar(p.getWeaponOutHeat(), p.getActiveWeapon().getMaxHeat());

        if (tabPressed) {
            draw.shadeScreen();
        }

        if (displayCollisions) drawCollisions();
    }

    private void drawCollisions() {
        Player p = new Player(gameData.getPlayer(playerID));
        glColor4f(1, 0, 0, 0.5f);
        for (int i = 0; i < width; i += 10) {
            for (int j = 0; j < height; j += 10) {
                p.setPos(new Vector2(i, j));
                if (!collisions.validPosition(p)) {
                    draw.drawCircle(i, height - j, 5, 5);
                }
            }
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
        drawPowerUps(oldPhase);

        GL11.glEnable(GL11.GL_STENCIL_TEST);

        glColorMask(false, false, false, false);
        glStencilFunc(GL_ALWAYS, 1, 0xFF); // Set any stencil to 1
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        glStencilMask(0xFF); // Write to stencil buffer
        glDepthMask(false); // Don't write to depth buffer
        glClear(GL_STENCIL_BUFFER_BIT); // Clear stencil buffer (0 by default)

        draw.drawCircle(pulse.getStart().getX(), height - pulse.getStart().getY(), pulse.getRadius(), 500);

        glStencilFunc(GL_EQUAL, 1, 0xFF); // Pass test if stencil value is 1
        glStencilMask(0x00); // Don't write anything to stencil buffer
        glDepthMask(true); // Write to depth buffer
        glColorMask(true, true, true, true);

        //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glColor3f(0, 0, 0);
        draw.drawCircle(pulse.getStart().getX(), height - pulse.getStart().getY(), pulse.getRadius(), 500);
        drawProjectiles(newPhase);
        map.renderMap(newPhase);
        drawOrbs(newPhase);
        drawPlayers(newPhase);
        drawPowerUps(newPhase);

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        pulse.draw();

    }

    private void drawPlayers(int phase) {
        ConcurrentHashMap<Integer, Player> players = gameData.getPlayers();
        int radius = players.get(0).getRadius();
        float red;
        float green;
        float blue;
        for (Player p : players.values()) {
            if (p.getPhase() == phase) {
                if (p.getTeam() == 0) {
                    red = 1;
                    green = 0.33f;
                    blue = 0.26f;
                } else {
                    red = 0.2f;
                    green = 0.9f;
                    blue = 0.5f;
                }
                draw.drawAura(p.getPos(), p.getRadius() + 10, 10, red - 0.2f, green - 0.2f, blue - 0.2f);
                GL11.glColor3f(red, green, blue);

                draw.drawCircle(p.getPos().getX(), height - p.getPos().getY(), radius, 100);

                if (p.getID() != playerID) {
                    positionBullet(new Vector2(p.getPos().getX(), height - p.getPos().getY()), p.getDir());
                } else {
                    Vector2 pos = new Vector2(p.getPos().getX(), height - p.getPos().getY());
                    Vector2 dir = getDirFromMouse(pos);
                    positionBullet(pos, dir);
                    p.setDir(dir);
                    conn.send(new MoveObject(p.getPos(), p.getDir(), playerID, p.getMoveCount()));
                }
            }
        }
    }

    private void drawOrbs(int phase) {
        HashMap<Integer, Orb> orbs = gameData.getOrbs();
        Player me = gameData.getPlayer(playerID);
        for (Orb o : orbs.values()) {
            if (phase == o.getPhase()) {
                draw.drawAura(o.getPos(), o.getRadius() + 5, 5, 0, 0, 0.8f);
                glColor4f(0.2f, 0.2f, 1f, 1);
                draw.drawCircle(o.getPos().getX(), height - o.getPos().getY(), o.getRadius(), 100);
            } else {
                float dist = me.getPos().getDistanceTo(o.getPos());
                if (dist < 150) {
                    float fade = 0.7f - (dist / 150f);
                    draw.drawAura(o.getPos(), o.getRadius() + 5, 5, 0, 0, 0.9f, fade);
                    glColor4f(0.2f, 0.2f, 1f, fade);
                    draw.drawCircle(o.getPos().getX(), height - o.getPos().getY(), o.getRadius(), 100);
                }
            }
        }
    }

    private void drawProjectiles(int phase) {
        ConcurrentHashMap<Integer, Projectile> projectiles = gameData.getProjectiles();
        float red;
        float green;
        float blue;
        for (Projectile p : projectiles.values()) {
            if (phase == p.getPhase()) {
                if (p.getTeam() == 0) {
                    red = 0.7f;
                    green = 0.1f;
                    blue = 0.1f;
                } else {
                    red = 0.1f;
                    green = 1f;
                    blue = 0.1f;
                }
                glColor3f(red, green, blue);
                float radius = p.getRadius();
                draw.drawCircle(p.getPos().getX(), height - p.getPos().getY(), radius, 100);
                draw.drawAura(p.getPos(), radius + radius / 2, radius / 2, red, green, blue);
            }
        }
    }

    private void drawPowerUps(int phase) {
        HashMap<Integer, PowerUp> powerUps = gameData.getPowerUps();
        float red;
        float green;
        float blue;
        for (PowerUp p : powerUps.values()) {
            if (phase == p.getPhase()) {
                if (p.getType() == PowerUp.Type.health) {
                    red = 0.7f;
                    green = 0.1f;
                    blue = 0.1f;
                } else {
                    red = 0.1f;
                    green = 1f;
                    blue = 0.1f;
                }
                float radius = p.getRadius();
                draw.drawQuad(p.getPos(), rotation, radius, red, green, blue);
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
