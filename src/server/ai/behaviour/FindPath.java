package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;
import server.ai.pathfinding.Node;
import server.game.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a behaviour where the entity maps a path to the target.
 * Created by rhys on 2/16/17.
 */
public class FindPath extends Task {

    private boolean lineOfSight;

    public FindPath(Intel intel, AIBrain brain) {
        super(intel, brain);
    }

    /**
     * Determines whether this path-finding object needs to
     * use A* search to find a path or not.
     * @param lineOfSight - set to true if the target is within line of sight
     *                    of the entity.
     */
    public void setSimplePath(boolean lineOfSight){
        this.lineOfSight = lineOfSight;
    }

    @Override
    public boolean checkConditions() {
        return (intel.ent().isAlive() &&
                intel.getTargetLocation() != null);
    }

    @Override
    public void doAction() {

       // System.out.println("Working out how to get there.");

        ArrayList<Vector2> path = new ArrayList<>();

        if (lineOfSight) {
            path.add(intel.getTargetLocation());
            intel.resetPath(path);
        }
        else {

            Node target=new Node(intel.getTargetLocation(),intel.ent().getRadius(),intel.ent().getPhase(),intel,intel.getTargetLocation());
            Node start=new Node(intel.ent().getPos(),intel.ent().getRadius(),intel.ent().getPhase(),intel,intel.getTargetLocation());

            intel.pathfinder().makeGraph(target,intel.ent().getPhase());


            Vector2 newTarget=intel.pathfinder.getClosestNode(target,intel.ent().getRadius());
            Vector2 newStart=intel.pathfinder.getClosestNode(start,intel.ent().getRadius());
            intel.pathfinder().AstarSearch(intel.pathfinder.getNode((int) newStart.getY(),(int) newStart.getX()),intel.pathfinder.getNode((int) newTarget.getY(),(int) newTarget.getX()));

            List<Node> printPath=intel.pathfinder().printPath(intel.pathfinder.getNode((int) newTarget.getY(),(int) newTarget.getX()));

            for (Node node:printPath ) {
                path.add(new Vector2(node.getX(), node.getY()));
            }


            intel.resetPath(path);
        }
        end();
    }
}
