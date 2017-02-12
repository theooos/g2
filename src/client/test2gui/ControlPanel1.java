package client.test2gui;

import client.testgui.TestEnvironment;
import server.game.Vector2;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Patrick on 2/10/2017.
 */
public class ControlPanel1 extends KeyAdapter {

    private TestEnvironment env;
    private int x;
    private int y;

    public ControlPanel1(TestEnvironment env) {

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
        env.movePlayer(new Vector2(x, y));
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
    SwingWorker worker = new SwingWorker<Void, Void>()
    {
        @Override
        public Void doInBackground() throws Exception
        {

            env.movePlayer(new Vector2(x, y));
            return null;
        }

    };

}



