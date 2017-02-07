package testbed;

import server.game.Vector2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ControlPanel extends KeyAdapter {

    private TestEnvironment env;

    public ControlPanel(TestEnvironment env) {

        super();
        this.env = env;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            env.movePlayer(new Vector2(0, -1));
        }
        else if (key == KeyEvent.VK_A) {
            env.movePlayer(new Vector2(-1, 0));

        }
        else if (key == KeyEvent.VK_S) {
            env.movePlayer(new Vector2(0, 1));

        }
        else if (key == KeyEvent.VK_D) {
            env.movePlayer(new Vector2(1, 0));
        }
    }
}