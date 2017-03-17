package server.ai.pathfinding;
/**
 * Created by Ciprian on 03/05/17.
 * Class corresponding to the edges between the matrices
 */
public class Edge{
    public final double cost;
    public final Node target;

    public Edge(Node targetNode, double costVal){
        target = targetNode;
        cost = costVal;
    }
}