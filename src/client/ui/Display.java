package client.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Created by theooos on 18/01/2017.
 */
public class Display {

    private JPanel content;
    private MainMenu menu;
    private Lobby lobby;

    public void displayUI() {

        JFrame frame = new JFrame("PhaseShift");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        content = new JPanel();
        content.setLayout(new CardLayout());

        menu = new MainMenu(content);
        lobby = new Lobby(content);

        content.add(menu, "Panel 1");
        content.add(lobby, "Panel 2");

        frame.setSize(700, 500);
        frame.setContentPane(content);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}

    class MainMenu extends JPanel
    {
        private JButton start;
        private JButton help;
        private JButton about;
        private JPanel menu;

        public MainMenu(JPanel content) {

            this.menu = content;
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

        @Override
        public Dimension getPreferredSize()
        {
            return (new Dimension(500, 500));
        }
    }

    class Lobby extends JPanel
    {
        private JTextField names;
        private JButton cancel;
        private JPanel lobby;

        public Lobby(JPanel content){

            this.lobby = content;
            setOpaque(true);

            JButton back = new JButton("Back");
            JTextField text = new JTextField();
            text.setText("Lol");

            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CardLayout cardLayout = (CardLayout) lobby.getLayout();
                    cardLayout.next(lobby);
                }
            });

            add(text, BorderLayout.CENTER);
            add(back, BorderLayout.CENTER);
        }

        @Override
        public Dimension getPreferredSize()
        {
            return (new Dimension(500, 500));
        }
    }