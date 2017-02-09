package client.testgui;


import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Runs a GUI for testing and developing the game's client to server communication
 * Created by peran on 8/02/17, heavily based off of rhys code
 */
public class TestUI {

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


    }

    
}
