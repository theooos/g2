package client.graphics;

import client.ClientSettings;
import objects.GameData;
import org.lwjgl.opengl.GL11;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static client.ClientSettings.ORB_VIS;

class GameRenderer {
    private GameData gameData;
    private int playerID;

    private MapRenderer map;
    private CollisionManager collisionManager;
    private boolean displayCollisions = false;

    private Draw draw;
    private Pulse pulse;

    float powerUpRotation = 0;

    /**
     * Sets up a new game renderer to show the game on screen
     * @param gameData all the game data
     * @param playerID this clients id
     * @param collisionManager how to handle collisions
     * @param textRenderers how to display text
     */
    GameRenderer(GameData gameData, int playerID, CollisionManager collisionManager, TextRenderer[] textRenderers) {
        this.gameData = gameData;
        this.playerID = playerID;
        this.collisionManager = collisionManager;

        map = new MapRenderer(gameData.getMapID());
        Player me = gameData.getPlayer(playerID);

        pulse = new Pulse(me.getPos(), me.getRadius(), me.getPhase(), 0, 1 - me.getPhase(), 20, 20, me.getPhase(), true);
        draw = new Draw(gameData, playerID, textRenderers);
    }

    /**
     * The main render method
     */
    void render() {
        Player p = gameData.getPlayer(playerID);
        int phase = p.getPhase();
        draw.colourBackground(phase);

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

        if (displayCollisions) drawCollisions();
    }

    /**
     * Draws the scoreboard
     * @param gameEnded if the game ended, shows victory or defeat banner
     */
    void drawScoreboard(boolean gameEnded) {
        draw.drawScoreboard(gameEnded);
    }

    /**
     * Whether to draw the collision mask
     */
    private void drawCollisions() {
        Player p = new Player(gameData.getPlayer(playerID));
        GL11.glColor4f(1, 0, 0, 0.5f);
        for (int i = 0; i < ClientSettings.SCREEN_WIDTH; i += 10) {
            for (int j = 0; j < ClientSettings.SCREEN_HEIGHT; j += 10) {
                p.setPos(new Vector2(i, j));
                if (!collisionManager.validPosition(p)) {
                    draw.drawCircle(i, ClientSettings.SCREEN_HEIGHT - j, 5, 5);
                }
            }
        }
    }

    /**
     * Draws the the stencil for the pulse, including the layer underneath
     */
    private void drawStencil() {
        int newPhase = pulse.getNewPhase();
        int oldPhase = 1;
        if (newPhase == 1) oldPhase = 0;

        //draws the old phase
        draw.colourBackground(oldPhase);
        drawProjectiles(oldPhase);
        map.renderMap(oldPhase);
        drawOrbs(oldPhase);
        drawPlayers(oldPhase);
        drawPowerUps(oldPhase);

        GL11.glEnable(GL11.GL_STENCIL_TEST);

        GL11.glColorMask(false, false, false, false);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF); // Set any stencil to 1
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GL11.glStencilMask(0xFF); // Write to stencil buffer
        GL11.glDepthMask(false); // Don't write to depth buffer
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT); // Clear stencil buffer (0 by default)

        //sets up the layer to be over drawn
        draw.drawCircle(pulse
                .getStart().getX(), ClientSettings.SCREEN_HEIGHT - pulse
                .getStart().getY(), pulse
                .getRadius(), 500);

        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF); // Pass test if stencil value is 1
        GL11.glStencilMask(0x00); // Don't write anything to stencil buffer
        GL11.glDepthMask(true); // Write to depth buffer
        GL11.glColorMask(true, true, true, true);

        //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glColor3f(0, 0, 0);
        draw.drawCircle(pulse
                .getStart().getX(), ClientSettings.SCREEN_HEIGHT - pulse
                .getStart().getY(), pulse
                .getRadius(), 500);

        //draws the new phase in the circle
        draw.colourBackground(newPhase);
        drawProjectiles(newPhase);
        map.renderMap(newPhase);
        drawOrbs(newPhase);
        drawPlayers(newPhase);
        drawPowerUps(newPhase);

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        pulse.draw();

    }

    /**
     * Draws the players on the given phase
     * @param phase which phase to draw players on
     */
    private void drawPlayers(int phase) {
        ConcurrentHashMap<Integer, Player> players = gameData.getPlayers();
        float red;
        float green;
        float blue;
        for (Player p : players.values()) {
            float radius = p.getRadius();
            //makes the radius a factor of phase percentage for phase animation
            radius = (phase == 0) ? radius * (1-p.getPhasePercentage()) : radius*p.getPhasePercentage();

            if (p.isAlive()) {
                if (p.getTeam() == 0) {
                    red = 0.71f;
                    green = 0.12f;
                    blue = 0.7f;
                } else {
                    red = 0f;
                    green = 1f;
                    blue = 0f;
                }
            } else {
                red = 0.6f;
                green = 0.6f;
                blue = 0.7f;
            }
            if (radius > 0) {
                draw.drawAura(p.getPos(), radius + 10, 10, red - 0.2f, green - 0.2f, blue - 0.2f);
            }

            //draws the player
            GL11.glColor3f(red, green, blue);
            draw.drawCircle(p.getPos().getX(), ClientSettings.SCREEN_HEIGHT - p.getPos().getY(), radius, 100);
            //adds the reticule for the weapon
            positionBullet(p, radius, red, green, blue);
        }
    }

    /**
     * Draws the reticule for the players
     * @param p the player to draw
     * @param radius the size of the reticule
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    private void positionBullet(Player p, float radius, float red, float green, float blue) {
        //gets the current position
        Vector2 pos = new Vector2(p.getPos().getX(), ClientSettings.SCREEN_HEIGHT - p.getPos().getY());
        Vector2 dir = p.getDir();
        //gets the direction to the cursor
        Vector2 cursor = pos.add((new Vector2(dir.getX(), 0 - dir.getY())).mult(21));
        float lastX = cursor.getX();
        float lastY = cursor.getY();

        if (lastX > 0 && lastY > 0) {
            //draw differently depending on weapon
            if (p.getActiveWeapon().toString().equals("SMG")) {
                draw.drawCircle(lastX, lastY, radius / 2, 50);
            } else {
                double ang = Math.atan(p.getDir().getX()/p.getDir().getY());
                if (Double.isInfinite(ang)) {
                    ang = 0;
                } else if (p.getDir().getY() < 0) {
                    ang += Math.PI;
                }
                if (p.getActiveWeapon().toString().equals("Shotgun")) {
                    draw.drawSpikes(new Vector2(lastX, ClientSettings.SCREEN_HEIGHT - lastY), (float) Math.toDegrees(ang), radius/1.8f, radius/2.6f, red, green, blue);
                } else {
                    draw.drawTriangle(new Vector2(lastX, ClientSettings.SCREEN_HEIGHT - lastY), (float) Math.toDegrees(ang), radius/1.2f, radius/1.8f, red, green, blue);
                }
            }
        }
    }

    /**
     * Draws the orbs
     * @param phase the phase to draw the orbs on
     */
    private void drawOrbs(int phase) {
        HashMap<Integer, Orb> orbs = gameData.getOrbs();
        Player me = gameData.getPlayer(playerID);
        float red;
        float green;
        float blue;
        //sets up the closest distance for the orbs
        float closestDist = ORB_VIS+1;
        //lists through all the orbs
        for (Orb o : orbs.values()) {
            float dist = me.getPos().getDistanceTo(o.getPos());
            if (dist > closestDist) closestDist = dist;

            //sets orb colours
            if (o.isAlive()) {
                red = 0.2f;
                green = 0.2f;
                blue = 1f;
            } else {
                red = 0.5f;
                green = 0.5f;
                blue = 0.7f;
            }
            //draws the orb if visible
            if (phase == o.getPhase()) {
                if (o.getRadius() > 0) {
                    draw.drawAura(o.getPos(), o.getRadius() + 5, 5, red - 0.1f, green - 0.1f, blue - 0.1f);
                }
                GL11.glColor4f(red, green, blue, 1);
                draw.drawCircle(o.getPos().getX(), ClientSettings.SCREEN_HEIGHT - o.getPos().getY(), o.getRadius(), 100);
            } else {
                //fade in the orb if on another phase
                if (dist < ORB_VIS) {
                    float fade = 0.7f - (dist / ORB_VIS);
                    if (o.getRadius() > 0) {
                        draw.drawAura(o.getPos(), o.getRadius() + 5, 5, red - 0.1f, green - 0.1f, blue - 0.1f, fade);
                    }
                    GL11.glColor4f(red, green, blue, fade);
                    draw.drawCircle(o.getPos().getX(), ClientSettings.SCREEN_HEIGHT - o.getPos().getY(), o.getRadius(), 100);
                }
            }
        }

        //AudioManager.playOrbHum(closestDist);
    }

    /**
     * Draws all the projectile
     * @param phase the phase to draw the projectiles on
     */
    private void drawProjectiles(int phase) {
        ConcurrentHashMap<Integer, Projectile> projectiles = gameData.getProjectiles();
        float red;
        float green;
        float blue;
        //steps through all the projectiles
        for (Projectile p : projectiles.values()) {
            if (phase == p.getPhase()) {
                //sets colours
                if (p.getTeam() == 0) {
                    red = 0.6f;
                    green = 0f;
                    blue = 0.6f;
                } else if (p.getTeam() == 1){
                    red = 0f;
                    green = 0.8f;
                    blue = 0f;
                } else {
                    //for orb damage
                    red = 0.2f;
                    green = 0.2f;
                    blue = 1f;
                }
                if (p.getDamage() == 0) {
                    red -= 0.34f;
                    green -= 0.34f;
                    blue -= 0.34f;
                }
                GL11.glColor3f(red, green, blue);
                float radius = p.getRadius();
                draw.drawCircle(p.getPos().getX(), ClientSettings.SCREEN_HEIGHT - p.getPos().getY(), radius, 100);
                draw.drawAura(p.getPos(), radius + radius / 2, radius / 2, red, green, blue);
            }
        }
    }

    /**
     * draws the powerups
     * @param phase the phase to draw the powerups int
     */
    private void drawPowerUps(int phase) {
        HashMap<Integer, PowerUp> powerUps = gameData.getPowerUps();
        float red;
        float green;
        float blue;
        //steps through all powerups
        for (PowerUp p : powerUps.values()) {
            if (phase == p.getPhase() && p.isAlive()) {
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
                draw.drawQuad(p.getPos(), powerUpRotation, radius, red, green, blue, true);
            }
        }
    }

    void setPulse(Pulse pulse) {
        this.pulse = pulse;
    }

    void flipDisplayCollisions() {
        displayCollisions = !displayCollisions;
    }


}