package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bianca on 06/02/2017.
 */
class MainMenu extends JPanel {
    private JButton start;
    private JButton help;
    private JButton about;
    private JPanel menu;

    public MainMenu(JPanel content) {

        this.menu = content;
        setOpaque(true);
        //setBackground(Color.RED.darker().darker());

        start = new JButton("Start");
        help = new JButton("Help");
        about = new JButton("About");

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) menu.getLayout();
                cardLayout.next(menu);
            }
        });

        add(start, BorderLayout.CENTER);
        add(help, BorderLayout.CENTER);
        add(about, BorderLayout.CENTER);
    }
}

