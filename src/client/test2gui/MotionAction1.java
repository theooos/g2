package client.test2gui;

import client.testgui.TestEnvironment;
import server.game.Vector2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Patrick on 2/10/2017.
 */
public class MotionAction1 extends AbstractAction implements ActionListener {

    private Vector2 dir;
    private TestEnvironment env;

    public MotionAction1(String name, TestEnvironment env, Vector2 dir){

        super(name);
        this.dir = dir;
        this.env = env;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        env.movePlayer(dir);

    }

}
