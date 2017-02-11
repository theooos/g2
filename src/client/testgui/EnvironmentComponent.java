package client.testgui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import objects.String;

/**
 * Created by rhys on 19/01/17.
 */
public class EnvironmentComponent extends JPanel {

    public EnvironmentComponent(TestEnvironment tenv){

        super();

        EnvironmentView view = new EnvironmentView(tenv);
        tenv.addObserver(view);
        String s = new String("fire");

        // listening to inputs from mouse and sending a message to server
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {

                tenv.sendFiringAlert(s);

            }
        });



        setLayout(new BorderLayout());
        add(view, BorderLayout.CENTER);
    }
}
