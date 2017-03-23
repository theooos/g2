package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;
import server.ai.pathfinding.Node;
import server.game.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * This behaviour allows an AI unit to determine a path towards a pre-determined
 * target point on the map. The behaviour will use an A* algorithm to find paths
 * towards points that aren't in direct line-of-sight, but the behaviour must be
 * prepared using the {@link #setSimplePath(boolean)} method, as it does not check
 * line of sight itself.
 *
 * Created by Rhys on 2/16/17.
 */
public class FindPath extends Task {

    private boolean lineOfSight;

    /**
     * Constructs a FindPath behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel the game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain the brain of the AI player that will be exhibiting this behaviour.
     */
    public FindPath(Intel intel, AIBrain brain) {
        super(intel, brain);
    }

    /**
     * Determines whether this path-finding object needs to
     * use A* search to find a path or not.
     *
     * @param lineOfSight set to true if the target is within line of sight
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
