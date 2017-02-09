package client.ui;


import javax.swing.*;
import java.awt.*;


/**
 * Created by theooos on 18/01/2017.
 */
public class Display {

    private MainMenu menu;
    private LobbyUI lobby;
    private JPanel content;

    public void displayUI() {

        JFrame frame = new JFrame("PhaseShift");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        content = new JPanel();
        content.setLayout(new CardLayout());

        menu = new MainMenu(content);
        lobby = new LobbyUI(content);

        content.add(menu, "Panel 1");
        content.add(lobby, "Panel 2");

        frame.setSize(700, 500);
        frame.setContentPane(content);
        //frame.pack();
        //frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}