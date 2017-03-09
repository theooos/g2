package client.AI;

import server.game.Map;
import server.game.Vector2;

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

    public Node(Vector2 position, int hVal){
        this.xValue=(int)position.getX();
        this.yValue=(int) position.getY();
        h_scores = hVal;
    }
    public boolean checkWall(Map map)
    {
        return true;
    }
    public int manhattanDistance(Vector2 enemy,boolean isWall)
    {
        if(!isWall)
            return abs((int)enemy.getX()-this.getX())+abs((int) enemy.getY()-this.getY());//calculates the absolute value of the manhattan distance
    return abs((int)enemy.getX()-this.getX())+abs((int) enemy.getY()-this.getY())-10000;//if the coordinates are walls then we will minimise the cost as much as possible
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
    public int getX()
    {
        return this.xValue;
    }
    public int getY()
    {
        return this.yValue;
    }
    public Vector2 coordinates(){
        return  new Vector2(xValue,yValue);
    }

}