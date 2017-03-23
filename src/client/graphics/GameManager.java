package client.graphics;

import client.ClientSettings;
import client.audio.AudioManager;
import networking.Connection_Client;
import objects.*;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import server.game.CollisionManager;
import server.game.Player;
import server.game.Vector2;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.ClientSettings.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Provides the visuals for the game itself.
 */
public class GameManager {

    enum Screen {GAME, MENU, SETTINGS, SCOREBOARD, GAMEOVER}

    private Screen screen = Screen.GAME;

    private GameRenderer gameRenderer;
    private InGameMenuRenderer inGameMenuRenderer;

    private boolean quitting = false;

    private long lastFrame;
    private int fps;
    private long lastFPS;
    private int myPlayerID;
    private Consumer<Object> endGameFunction;

    private GameData gameData;
    private Connection_Client conn;
    private CollisionManager collisions;

    public GameManager(GameData gd, Connection_Client conn, int playerID, TextRenderer[] textRenderers, Consumer<Object> endGameFunction) {
        super();
        this.conn = conn;
        this.gameData = gd;
        this.myPlayerID = playerID;
        this.endGameFunction = endGameFunction;

        collisions = new CollisionManager(gd);
        gameRenderer = new GameRenderer(gameData, playerID, collisions, textRenderers);
        inGameMenuRenderer = new InGameMenuRenderer(this);

        conn.addFunctionEvent("GameOver", this::gameOver);
    }

    public void run() {
        switch (screen) {
            case GAME:
                update();
                gameRenderer.render();
                break;
            case MENU:
                update();
                glEnable(GL_TEXTURE_2D);
                glColor4f(1, 1, 1, 1);
                inGameMenuRenderer.renderMenu();
                inGameMenuRenderer.handleClickedMenu();
                break;
            case SETTINGS:
                glEnable(GL_TEXTURE_2D);
                glColor4f(1, 1, 1, 1);
                inGameMenuRenderer.renderSettings();
                inGameMenuRenderer.handleClickedSettings();
                SettingsRenderer.run(e -> screen = Screen.MENU);
                break;
            case SCOREBOARD:
                update();
                gameRenderer.render();
                gameRenderer.drawScoreboard(false);
                break;
            case GAMEOVER:
                update();
                gameRenderer.render();
                gameRenderer.drawScoreboard(true);
                quit();
        }
    }

    void quit() {
        if (!quitting) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    endGameFunction.accept(null);
                }
            }, ClientSettings.ENG_GAME_TIME);
            quitting = true;
        }
    }

    private void update() {
        if (gameData.getPlayer(myPlayerID).isAlive()) makeMovement();
        pollKeyboard();
        pollMouse();
        AudioManager.playWarningSounds(gameData.getPlayer(myPlayerID).getHealth());
        rotatePowerUps();
        //updateFPS(); // update FPS Counter
    }

    private void rotatePowerUps() {
        gameRenderer.powerUpRotation += 1.5f;
        gameRenderer.powerUpRotation %= 360;
    }

    private void pollMouse() {
        Player me = gameData.getPlayer(myPlayerID);
        if (me.isAlive()) {
            while (Mouse.next()) {
                switch (Mouse.getEventButton()) {
                    case 0:
                        if (Mouse.getEventButtonState()) {
                            conn.send(new FireObject(me.getID(), true));
                        } else {
                            conn.send(new FireObject(me.getID(), false));
                        }
                }
            }
        }
    }

    private void makeMovement() {
        Player me = gameData.getPlayer(myPlayerID);
        Vector2 pos = me.getPos();

        float xPos = pos.getX();
        float yPos = pos.getY();

        int delta = getDelta();

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) xPos -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) xPos += 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) yPos -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) yPos += 0.35f * delta;

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
            conn.send(new MoveObject(me.getPos(), me.getDir(), myPlayerID, me.getMoveCount()));
            gameData.updatePlayer(me);
        } else if (me.getPos().equals(pos)) {
            conn.send(new MoveObject(me.getPos(), me.getDir(), myPlayerID, me.getMoveCount()));
        }
    }

    private void pollKeyboard() {
        while (Keyboard.next()) {
            // Runs if next key has been PRESSED.
            if (Keyboard.getEventKeyState()) {
                switch (screen) {
                    case GAME:
                        gameKeyboard();
                        break;
                    default:
                        menuKeyboard();
                }
            } else {
                switch (screen) {
                    case SCOREBOARD:
                        if (Keyboard.getEventKey() == Keyboard.KEY_TAB) {
                            screen = Screen.GAME;
                        }
                }
            }
        }
    }

    private void gameKeyboard() {
        Player me = gameData.getPlayer(myPlayerID);
        switch (Keyboard.getEventKey()) {

            // *** Those that depend on being alive ***
            case Keyboard.KEY_E:
                if (me.isAlive()) conn.send(new SwitchObject(me.getID(), !me.isWeaponOneOut()));
                break;

            case Keyboard.KEY_F:
            case Keyboard.KEY_SPACE:
                if (me.isAlive()) {
                    AudioManager.playPulseSound();
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
                break;

            case Keyboard.KEY_1:
                if (me.isAlive()) conn.send(new SwitchObject(me.getID(), true));
                break;

            case Keyboard.KEY_2:
                if (me.isAlive()) conn.send(new SwitchObject(me.getID(), false));
                break;

            // *** Those that don't depend on being alive ***

            case Keyboard.KEY_M:
                if (SOUND_VOL == 0) {
                    MUSIC_VOL = 1;
                    SOUND_VOL = 1;
                } else {
                    SOUND_VOL = 0;
                    MUSIC_VOL = 0;
                    AudioManager.muteEverything();
                }
                break;

            case Keyboard.KEY_C:
                gameRenderer.flipDisplayCollisions();
                break;

            case Keyboard.KEY_ESCAPE:
                screen = Screen.MENU;
                break;

            case Keyboard.KEY_TAB:
                if (screen != Screen.GAMEOVER) screen = Screen.SCOREBOARD;
                break;
        }
    }

    private void menuKeyboard() {
        switch (Keyboard.getEventKey()) {
            case Keyboard.KEY_ESCAPE:
                screen = Screen.GAME;
                break;
        }
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

    private void gameOver(Sendable sendable) {
        gameData.updateScoreboard(((GameOver) sendable).getScoreboard());
        screen = Screen.GAMEOVER;
        AudioManager.playGameOver();
    }

    void setScreen(Screen screen) {
        this.screen = screen;
    }

    public static void out(Object o) {
        if (DEBUG) System.out.println(o);
    }
}
