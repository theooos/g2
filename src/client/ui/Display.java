package client.ui;

import javax.swing.*;
import java.awt.*;


/**
 * Created by theooos on 18/01/2017.
 */
public class Display {

    private JFrame frame;
    private Container pane;
    private MainMenu menu;
    private LobbyUI lobby;

    public void displayUI() {

        frame = new JFrame("PhaseShift");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        pane = frame.getContentPane();

        MainMenu menu = new MainMenu();
        this.menu = menu;
        menu.createMenu(pane);

        frame.setVisible(true);
    }

    public void paintLobby(){
        pane.removeAll();
        this.lobby = new LobbyUI();
        lobby.createLobby(pane, menu.getClientUsername());
    }

    public void paintMenu(){
        pane.removeAll();
        this.menu = new MainMenu();
        menu.createMenu(pane);
    }
}