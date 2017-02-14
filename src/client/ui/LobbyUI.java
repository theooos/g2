package client.ui;

import client.Client;
import client.ClientLogic.ClientReceiver;
import client.ClientLogic.ClientSendable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by bianca on 06/02/2017.
 */
class LobbyUI extends JPanel {

    private static Color mustard = new Color(245, 225, 65);
    private static Color background = new Color(45,60,75);

    private JTextArea clientNames;
    private JButton cancelButton;
    private JLabel clientText;
    private JLabel countDownText;
    private JTextArea countDown;
    private JButton mapIcon;

    public void createLobby(Container pane, String clientUsername){

        Client client = new Client();
        //client.connection.send();
        ClientSendable cs = new ClientSendable(client.connection);
        //ClientReceiver cr = new ClientReceiver(client.connection);

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        clientText = new JLabel("Users connected");

        clientNames = new JTextArea(clientUsername);
        clientNames.setMinimumSize(new Dimension(200, 300));
        clientNames.setMinimumSize(new Dimension(200, 300));
        clientNames.setEnabled(false);

        cancelButton = new JButton("Cancel");

        countDownText = new JLabel("Game will start in");

        countDown = new JTextArea("");
        countDown.setEnabled(false);

        mapIcon = new JButton();
        ImageIcon iconLogo = new ImageIcon("Users/Bianca/Documents/map.jpg");
        mapIcon.setIcon(iconLogo);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideLobby();
                objects.String s = new objects.String("fire");
                cs.sendFiringAlert(s);
                pane.removeAll();
                new MainMenu().createMenu(pane);
            }
        });



        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        pane.add(clientText, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 50;
        c.ipadx = 50;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        pane.add(clientNames, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        pane.add(countDownText, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady= 50;
        c.ipadx = 50;
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 3;
        pane.add(mapIcon, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 3;
        c.gridy = 1;
        pane.add(countDown, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 2;
        pane.add(cancelButton, c);
    }

    private void hideLobby(){
        clientNames.setVisible(false);
        cancelButton.setVisible(false);
        clientText.setVisible(false);
        countDownText.setVisible(false);
        countDown.setVisible(false);
        mapIcon.setVisible(false);
    }

    private void showLobby(String clientUsername){
        clientNames.setText(clientUsername);
        clientNames.setVisible(true);
        cancelButton.setVisible(true);
        clientText.setVisible(true);
    }
}