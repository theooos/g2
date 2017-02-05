package client.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Created by theooos on 18/01/2017.
 */
public class Display {

    private JFrame frame;

    public void displayUI() {

        frame = new JFrame("PhaseShift");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        Container pane = frame.getContentPane();

        MainMenu menu = new MainMenu();
        menu.createMenu(pane, new LobbyUI());

        //LobbyUI lobby = new LobbyUI();
        //lobby.createLobby(pane, new MainMenu(), "");
        frame.setVisible(true);
    }
}