package server.ai.behaviour;

import server.ai.Intel;
import server.game.Vector2;

import java.util.ArrayList;

/**
 * Defines a behaviour where the entity maps a path to the target.
 * Created by rhys on 2/16/17.
 */
public class FindPath extends Task {

    public FindPath(Intel intel) {
        super(intel);
    }

    @Override
    public boolean checkConditions() {
        return (intel.ent().isAlive() &&
                intel.getTargetLocation() != null);
    }

    @Override
    public void doAction() {
        // SKELETON METHOD.
        // PERQUISITE: Collision Detection, DoorwayDetection.
        ArrayList<Vector2> path = new ArrayList<>();
        path.add(intel.getTargetLocation());
        intel.resetPath(path);
        System.out.println("Path found");
        this.curRunState = runState.FINISHED;
    }


}
