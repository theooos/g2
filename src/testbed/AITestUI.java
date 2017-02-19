package testbed;
import javax.swing.*;

/**
 * Runs a GUI for testing and developing the game's AI algorithms and implementations.
 * Created by rhys on 19/01/17.
 */
public class AITestUI {

    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT= 800;
    public static final boolean DEBUG = true;

    public static void main(String[] args){

        // Create and configure the JFrame.
        JFrame frame = new JFrame("AI Test");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TestEnvironment testEnv = new TestEnvironment();
        EnvironmentComponent comp = new EnvironmentComponent(testEnv);
        frame.add(comp);

        frame.setVisible(true);

    }
    
}
