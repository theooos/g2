package client.graphics;

import client.ClientSettings;
import client.audio.Audio;
import objects.*;
import networking.Connection;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import server.game.*;

/**
 * Provides the visuals for the game itself.
 */
public class GameManager {

    private GameRenderer gameRenderer;

    private enum Mode {GAME,MENU,SCOREBOARD,GAMEOVER}
    private Mode mode = Mode.GAME;

    private long lastFrame;
    private int fps;
    private long lastFPS;
    private int playerID;

    private GameData gameData;
    private Connection conn;
    private CollisionManager collisions;

    private boolean fDown;
    private boolean clickDown;
    private boolean eDown;
    private boolean oneDown;
    private boolean twoDown;
    private boolean healthbar;
    private boolean gameMusic;
    private boolean muted;

    public GameManager(GameData gd, Connection conn, int playerID) {
        super();
        this.conn = conn;
        this.gameData = gd;
        this.playerID = playerID;

        fDown = false;
        clickDown = false;
        eDown = false;
        oneDown = false;
        twoDown = false;
        healthbar = true;
        gameMusic = false;
        muted = true;

        collisions = new CollisionManager(gd);
        gameRenderer = new GameRenderer(gameData,playerID,collisions);

        conn.addFunctionEvent("GameOver", this::gameOver);
    }

    private void gameOver(Sendable sendable) {
        gameData.updateScoreboard(((GameOver) sendable).getScoreboard());
        mode = Mode.GAMEOVER;
    }

    public void run(){
        switch (mode){
            case GAME:
                update();
                gameRenderer.render();
                break;
            case MENU:
                //TODO Render in-game menu
                break;
            case SCOREBOARD:
                //TODO Show the scoreboard
                break;
            case GAMEOVER:
                TextRenderer textRenderer = new TextRenderer();
                textRenderer.drawText("Game over. To be changed.",0,0);
        }
    }

    private void update() {
        gameRenderer.rotation += 1.5f;
        gameRenderer.rotation %= 360;
        Player me = gameData.getPlayer(playerID);
        checkMusic(me);

        if (me.isAlive()) {

            Vector2 pos = me.getPos();

            float xPos = pos.getX();
            float yPos = pos.getY();

            int delta = getDelta();

            if (Keyboard.isKeyDown(Keyboard.KEY_A)) xPos -= 0.35f * delta;
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) xPos += 0.35f * delta;
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) yPos -= 0.35f * delta;
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) yPos += 0.35f * delta;

            if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
                if (!muted) {
                    muted = true;
                } else {
                    muted = false;
                    muteEverything();
                }
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
                fDown = true;
                if (muted) Audio.PHASE.play();
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
                    gameRenderer.setPulse(new Pulse(me.getPos(), me.getRadius(), newPhase, 0, 1 - newPhase, 20, 20, newPhase, true));
                } else {
                    //invalid phase
                    gameRenderer.setPulse(new Pulse(me.getPos(), me.getRadius(), 0.3f, 0.3f, 0.3f, 20, 20, me.getPhase(), 250, false));
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
                gameRenderer.flipDisplayCollisions();
            }

            if (Mouse.isButtonDown(0)) {
                if (!clickDown) {
                    conn.send(new FireObject(me.getID(), true));
                    clickDown = true;
                }
            } else if (clickDown) {
                if (me.activeWeapon() == 1 && muted)
                    Audio.SHOOT2.play();
                else if (muted) {
                    Audio.SHOOT.play();
                }
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
                } else {
                    me.setPos(pos);
                }
            }

            Vector2 tempPos = new Vector2(pos.getX(), ClientSettings.SCREEN_HEIGHT - pos.getY());
            Vector2 dir = getDirFromMouse(tempPos);
            if (!me.getDir().equals(dir)) {
                me.setDir(dir);
                conn.send(new MoveObject(me.getPos(), me.getDir(), playerID, me.getMoveCount()));
                gameData.updatePlayer(me);
            } else if (me.getPos().equals(pos)) {
                conn.send(new MoveObject(me.getPos(), me.getDir(), playerID, me.getMoveCount()));
            }
        }

        updateFPS(); // update FPS Counter
    }

    private Vector2 getDirFromMouse(Vector2 pos) {
        Vector2 mousePos = new Vector2(Mouse.getX(), Mouse.getY());
        Vector2 dir = pos.vectorTowards(mousePos);
        dir = dir.normalise();
        return new Vector2(dir.getX(), 0 - dir.getY());
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

    private void checkMusic(Player me) {
        if (me.getHealth() < 25 && healthbar && muted) {
            healthbar = false;
            gameMusic = true;
            Audio.GAMEMUSIC.stopClip();
            Audio.WARNING.playallTime();
        } else if (me.getHealth() > 25 && gameMusic && muted) {
            gameMusic = false;
            healthbar = true;
            Audio.WARNING.stopClip();
            Audio.GAMEMUSIC.playallTime();
        } else if (muted) {
            Audio.GAMEMUSIC.playallTime();
        }
    }

    private void muteEverything() {
        Audio.SHOOT.stopClip();
        Audio.SHOOT.stopClip();
        Audio.GAMEMUSIC.stopClip();
        Audio.WARNING.stopClip();
    }
}
