package client.graphics;

import client.ClientLogic.ClientSendable;
import client.ClientLogic.GameData;
import networking.Connection;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import server.game.Player;
import server.game.Vector2;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by bianca on 14/02/2017.
 */
public class GameRenderer {

    private String WINDOW_TITLE = "PhaseShift";
    public static boolean gameRunning = true;
    private int width = 800;
    private int height = 600;

    private long lastFrame;
    private int fps;
    private long lastFPS;
    private int playerID;

    private GameData gd;
    private HashMap<Integer,Player> players;
    private MapRenderer map;
    private Connection conn;
    private ClientSendable cs;

    public GameRenderer(GameData gd, Connection conn) {

        this.conn = conn;
        this.gd = gd;
        players = gd.getPlayers();
        cs = new ClientSendable(conn);

        // initialize the window beforehand
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(WINDOW_TITLE);
            Display.create();

            System.err.print("Creating the display!");

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, width, 0, height, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            map = new MapRenderer(gd.getMapID(), 1);

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

            Display.update();
            Display.sync(60); // cap fps to 60fps
        }
        // clean up
        Display.destroy();
    }

    public void DrawCircle(float cx, float cy, float r, int num_segments)
    {
        float theta = (float)(2 * 3.1415926 / (num_segments));
        float tangetial_factor = (float)Math.tan(theta);//calculate the tangential factor
        float radial_factor = (float)Math.cos(theta);//calculate the radial factor

        float x = r;//we start at angle = 0
        float y = 0;

        GL11.glBegin(GL_TRIANGLE_FAN);

        for(int ii = 0; ii < num_segments; ii++)
        {
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

    float lastX=-1;
    float lastY=-1;

    private void positionBullet(Vector2 pos)
    {
        Vector2 mousePos=new Vector2(Mouse.getX(),Mouse.getY()) ;
        System.out.println("MousePos"+mousePos);

        Vector2 dir=pos.vectorTowards(mousePos);
        System.out.println("dir"+dir);

        Vector2 cursor= dir.normalise();
        System.out.println("cursor"+cursor);

        cursor=pos.add(cursor.mult(21));
        lastX =cursor.getX();//pos.getX()+(Mouse.getX()-pos.getX())/10;
        lastY = cursor.getY();//pos.getY()+(Mouse.getY()-pos.getY())/10;
        //Mouse.setGrabbed(true);

        if (lastX>0&&lastY>0)
            DrawCircle(lastX,lastY,10,50);
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void update(int delta) {

        float xPos = players.get(playerID).getPos().getX();
        float yPos = players.get(playerID).getPos().getY();

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) xPos -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) xPos += 0.35f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) yPos -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) yPos += 0.35f * delta;

        // keep quad on the screen
        if (xPos < 0) xPos = 0;
        if (xPos > 800) xPos = 800;
        if (yPos < 0) yPos = 0;
        if (yPos > 600) yPos = 600;

        players.get(playerID).setPos(new Vector2(xPos, yPos));
        gd.updatePlayers(players.get(playerID));
        cs.sendPlayer(players.get(playerID));


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

        map.renderMap();
        drawPlayers();

        /* update movement
        glBegin(GL11.GL_QUADS);

        glVertex2f(xPos - 50, yPos - 50);
        glVertex2f(xPos + 50, yPos - 50);
        glVertex2f(xPos + 50, yPos + 50);
        glVertex2f(xPos - 50, yPos + 50);

        GL11.glEnd();
        GL11.glPopMatrix();
        */
    }

    private void drawPlayers(){
        for(Player p: players.values()) {
            if (p.getID() % 2 == 0) {
                GL11.glColor3f(1, 0.33f, 0.26f);
                DrawCircle(p.getPos().getX(), height - p.getPos().getY(), 20, 100);
                positionBullet(new Vector2(p.getPos().getX(),height - p.getPos().getY()));

            } else {
                GL11.glColor3f(0.2f, 0.9f, 0.5f);
                DrawCircle(p.getPos().getX(), height - p.getPos().getY(), 20, 100);
                positionBullet(new Vector2(p.getPos().getX(),height - p.getPos().getY()));
            }
        }
    }

    private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    public void setID(int id)
    {
        this.playerID = id;
    }

    public static void main(String argv[]) {

        //we need to create a GameData
        //new GameRenderer .execute();
        System.exit(0);
    }
}
