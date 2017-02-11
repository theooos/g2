package client.testgui;

import server.game.Vector2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

public class ControlPanel extends KeyAdapter {

    private TestEnvironment env;
    private int x;
    private int y;

    public ControlPanel(TestEnvironment env) {

        super();
        this.env = env;
        x = 0;
        y = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
           y = -1;
        }
        else if (key == KeyEvent.VK_A) {
            x = -1;

        }
        else if (key == KeyEvent.VK_S) {
            y = 1;

        }
        else if (key == KeyEvent.VK_D) {
            x = 1;
        }

        else if(key == KeyEvent.VK_N)
        {
            objects.String s = new objects.String("ID:153");
            env.sendID(s);

        }
        env.movePlayer(new Vector2(x, y));
        env.sendPosition(new Vector2(x,y));
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            y = 0;
        }
        else if (key == KeyEvent.VK_A) {
            x = 0;
        }
        else if (key == KeyEvent.VK_S) {
            y = 0;

        }
        else if (key == KeyEvent.VK_D) {
            x = 0;
        }
    }





}