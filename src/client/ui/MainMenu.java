package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bianca on 06/02/2017.
 */
class MainMenu extends JPanel {

    private static Color mustard = new Color(245, 225, 65);
    private static Color background = new Color(45,60,75);

    private JButton start, help, about;
    private JLabel userLabel;
    private JTextField userText;
    private JButton loginButton;
    private String clientUsername;

    public void createMenu(Container pane) {

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setBackground(background);

        start = new JButton("Start");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideMenu();
                pane.removeAll();
                createLogin(pane);
            }
        });

        help = new JButton("Help");
        help.setAlignmentX(Component.CENTER_ALIGNMENT);

        about = new JButton("About");
        about.setAlignmentX(Component.CENTER_ALIGNMENT);

        pane.add(Box.createRigidArea(new Dimension(100, 200)));
        pane.add(start);
        pane.add(Box.createRigidArea(new Dimension(100, 20)));
        pane.add(help);
        pane.add(Box.createRigidArea(new Dimension(100, 20)));
        pane.add(about);
    }

    private void createLogin(Container pane){
        userLabel = new JLabel("User name:");
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userText = new JTextField(1);
        userText.setAlignmentX(Component.CENTER_ALIGNMENT);
        userText.setMaximumSize(new Dimension(400, 30));

        loginButton = new JButton("GO");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientUsername = userText.getText();
                hideLogin();
                pane.removeAll();
                new LobbyUI().createLobby(pane, clientUsername);
            }
        });

        pane.add(Box.createRigidArea(new Dimension(100, 200)));
        pane.add(userLabel);
        pane.add(Box.createRigidArea(new Dimension(100, 20)));
        pane.add(userText);
        pane.add(Box.createRigidArea(new Dimension(100, 20)));
        pane.add(loginButton);
    }

    private void hideMenu() {
        start.setVisible(false);
        help.setVisible(false);
        about.setVisible(false);
    }

    private void showMenu() {
        start.setVisible(true);
        help.setVisible(true);
        about.setVisible(true);
    }

    private void hideLogin(){
        userLabel.setVisible(false);
        userText.setVisible(false);
        loginButton.setVisible(false);
    }

    private void showLogin(){
        userLabel.setVisible(true);
        userText.setVisible(true);
        loginButton.setVisible(true);
    }

    public String getClientUsername(){
        return clientUsername;
    }
}

