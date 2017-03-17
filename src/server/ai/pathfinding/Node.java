package server.ai.pathfinding;

import server.ai.Intel;
import server.game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.abs;

/**
 * Created by Ciprian on 03/05/17.
 * Class creates nodes that correspond to the x,y coordinates.
 */
public class Node{

    public int xValue;
    public int yValue;
    public double g_scores;
    public  int h_scores;
    public double f_scores = 0;
   public ArrayList<Edge> adjacencies= new ArrayList<>();
   // public Edge[] ;
    public Node parent;
    public int radius;
    public int phase;
    private CollisionManager collisions;

    /**
     * Constructor for Node
     * @param position a Vector2 corresponding to the position of the Node on the map
     * @param radius radius of the entity
     * @param phase phase of the entity
     * @param intel information for the player
     * @param enemy position of the target it is supposed to be reported to
     */
    public Node(Vector2 position, int radius,int phase,Intel intel,Vector2 enemy){
        this.xValue=(int)position.getX();
        this.yValue=(int) position.getY();
        this.phase=phase;
        this.radius=radius;
        ConcurrentHashMap<Integer,Player> players=intel.getPlayers();
        HashMap<Integer,Orb> orbs=intel.getOrbs();
        Map map=intel.getMap();
        this.collisions=new CollisionManager(players,orbs,map);
        h_scores=manhattanDistance(enemy);
    }

    /**
     * adds edges to a node
     * @param moreEdges
     */
    public void addAdjancencies(ArrayList<Edge> moreEdges)
    {
        this.adjacencies.addAll(moreEdges);
    }

    /**
     * adds an edge to the node
     * @param newEdge
     */
    public void addAdjancency(Edge newEdge)
    {

        this.adjacencies.add(newEdge);
    }

    /**
     *
     * @return wheter or not the node is a valid position
     */
    public boolean checkCollision()
    {
        return collisions.validPosition(coordinates(),getRadius()*3,getPhase());
    }

    /**
     * calculates the heuristic for the Astar cost function
     * @param enemy coordinates of the enemy
     * @return the cost value of a node
     */
    public int manhattanDistance(Vector2 enemy)
    {

        if(checkCollision())
            return abs((int)enemy.getX()-this.getX())+abs((int) enemy.getY()-this.getY());//calculates the absolute value of the manhattan distance
    return abs((int)enemy.getX()-this.getX())+abs((int) enemy.getY()-this.getY())+10000;//if the coordinates are walls then we will minimise the cost as much as possible
    }

    /**
     *  getters and setters
     */

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
    public void setAdjacencies(ArrayList<Edge> edges)
    {
        this.adjacencies=edges;
    }
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