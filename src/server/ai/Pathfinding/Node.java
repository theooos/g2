package server.ai.Pathfinding;

import server.ai.Intel;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.abs;

/**
 * Created by Ciprian on 03/05/17.
 */
public class Node{

    public int xValue;
    public int yValue;
    public double g_scores;
    public  int h_scores;
    public double f_scores = 0;
    public Edge[] adjacencies;
    public Node parent;
    public int radius;
    public int phase;
    private CollisionManager collisions;

    public Node(Vector2 position, int hVal,int radius,int phase,Intel intel){
        this.xValue=(int)position.getX();
        this.yValue=(int) position.getY();
        this.phase=phase;
        this.radius=radius;
        h_scores = hVal;
        ConcurrentHashMap<Integer,Player> players=intel.getPlayers();
        HashMap<Integer,Orb> orbs=intel.getOrbs();
        Map map=intel.getMap();
        this.collisions=new CollisionManager(players,orbs,map);

    }
    public boolean checkCollision()
    {
        return collisions.validPosition(coordinates(),getRadius(),getRadius());
    }
    public int manhattanDistance(Vector2 enemy)
    {
        if(!checkCollision())
            return abs((int)enemy.getX()-this.getX())+abs((int) enemy.getY()-this.getY());//calculates the absolute value of the manhattan distance
    return abs((int)enemy.getX()-this.getX())+abs((int) enemy.getY()-this.getY())+10000;//if the coordinates are walls then we will minimise the cost as much as possible
    }
    public void setX(int x)
    {
        this.xValue=x;
    }

    public void setY(int y)
    {
        this.yValue=y;
    }
    public void setH_scores(int hScores)
    {
        this.h_scores=hScores;
    }
    public void setRadius(int radius){this.radius=radius;}
    public void setPhase(int phase){this.phase=phase;}
    public int getX()
    {
        return this.xValue;
    }
    public int getY()
    {
        return this.yValue;
    }
    public int getRadius(){return this.radius;}
    public int getPhase(){return this.phase;}
    public Vector2 coordinates(){
        return  new Vector2(xValue,yValue);
    }

}