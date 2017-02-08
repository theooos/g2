package client.testgui;
import javax.swing.*;
import java.awt.*;

/**
 * Created by rhys on 19/01/17.
 */
public class EnvironmentComponent extends JPanel {

    public EnvironmentComponent(TestEnvironment env){

        super();

        EnvironmentView view = new EnvironmentView(env);
        env.addObserver(view);

        setLayout(new BorderLayout());
        add(view, BorderLayout.CENTER);
    }
}
