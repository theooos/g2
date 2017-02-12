package client.test2gui;

import client.testgui.ControlPanel;
import client.testgui.TestEnvironment;
import server.game.MovableEntity;
import server.game.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Patrick on 2/10/2017.
 */
public class EnvironmentView1 extends JPanel  {

    private TestEnvironment env;
    private int size;

    public EnvironmentView1(TestEnvironment env){
        super();
        this.env = env;

        addKeyListener(new ControlPanel(env));
        setBackground(Color.white);
        setFocusable(true);
        setDoubleBuffered(true);

    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;

        int width = env.getMapWidth();
        int height = env.getMapHeight();
        g2.clearRect(0,0, width, height);

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        ArrayList<MovableEntity> es = new ArrayList<>(env.getEntities());
        if (es.size() != size) {
            System.out.println("Size: " + es.size());
            size = es.size();
        }
        for (MovableEntity e: es) {
            switch (e.getTeam()) {
                case 0:
                    g2.setColor(Color.RED);
                    break;
                case 1:
                    g2.setColor(Color.BLUE);
                    break;
                default:
                    g2.setColor(Color.BLACK);
                    break;
            }
            Ellipse2D.Double p = entityToSpot(e);
            g2.fill(p);
            g2.setColor(Color.BLACK);
            g2.draw(p);
        }

        ArrayList<Line2D.Double> lines = wallsToLines();
        for (Line2D line : lines){
            g2.draw(line);
        }

        repaint();
    }

    /*
    public void update(Observable obs, Object obj){

        System.out.println("New Position: " + env.getPlayer().getPos().toString());

        repaint();
    }

    */
    private Ellipse2D.Double entityToSpot(MovableEntity entity){
        double r = entity.getRadius();
        double xCorner = entity.getPos().getX() - r;
        double yCorner = entity.getPos().getY() - r;

        //System.out.println("ZOMB: " + xCorner + ", " + yCorner);

        return new Ellipse2D.Double(xCorner, yCorner, r*2, r*2);
    }

    private ArrayList<Line2D.Double> wallsToLines(){
        ArrayList<Line2D.Double> lines = new ArrayList<>();
        for (Wall wall : env.getWalls()){
            double x1 = wall.getStartPos().getX();
            double y1 = wall.getStartPos().getY();
            double x2 = wall.getEndPos().getX();
            double y2 = wall.getEndPos().getY();
            lines.add(new Line2D.Double(x1, y1, x2, y2));
        }
        return lines;
    }
}

