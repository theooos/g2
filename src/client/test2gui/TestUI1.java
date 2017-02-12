package client.test2gui;

import client.testgui.EnvironmentComponent;
import client.testgui.TestEnvironment;

import javax.swing.*;
import java.util.*;

/**
 * Created by Patrick on 2/10/2017.
 */

public class TestUI1 {

    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT= 1000;

    public static void main(String[] args){

        // Create and configure the JFrame.
        JFrame frame = new JFrame("Client Test");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TestEnvironment testEnv = new TestEnvironment();
        EnvironmentComponent comp = new EnvironmentComponent(testEnv);
        frame.add(comp);

        frame.setVisible(true);

        java.util.Timer t = new java.util.Timer();

        int rate = 1000/60;

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                comp.repaint();
            }
        }, rate, rate);

    }


}

