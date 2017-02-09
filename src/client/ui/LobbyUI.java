package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bianca on 06/02/2017.
 */
class LobbyUI extends JPanel {

    private JLabel userLabel;
    private JTextField userText, clientNames;
    private JButton loginButton, cancelButton;
    private JPanel lobby;

    private java.lang.String userName;

    public LobbyUI(JPanel content) {

        this.lobby = content;
        setOpaque(true);

        userLabel = new JLabel("User name:");
        add(userLabel);

        userText = new JTextField(20);
        add(userText);

        loginButton = new JButton("GO");
        add(loginButton);

        clientNames = new JTextField(userName);
        add(clientNames, BorderLayout.CENTER);

        cancelButton = new JButton("Cancel");
        add(cancelButton, BorderLayout.CENTER);

        setLoginVisible();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLobbyVisible();
                clientNames.setText(userText.getText());
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) lobby.getLayout();
                cardLayout.next(lobby);
                setLoginVisible();
            }
        });

    }

    private void setLoginVisible() {
        userLabel.setVisible(true);
        userText.setVisible(true);
        loginButton.setVisible(true);
        userText.setText("");

        clientNames.setVisible(false);
        cancelButton.setVisible(false);
    }

    private void setLobbyVisible() {
        userLabel.setVisible(false);
        userText.setVisible(false);
        loginButton.setVisible(false);

        clientNames.setVisible(true);
        cancelButton.setVisible(true);
    }
}
