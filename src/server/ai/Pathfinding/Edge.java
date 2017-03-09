package server.ai.Pathfinding;
/**
 * Created by Ciprian on 03/05/17.
 */
public class Edge{
    public final double cost;
    public final Node target;

    public Edge(Node targetNode, double costVal){
        target = targetNode;
        cost = costVal;
    }
}