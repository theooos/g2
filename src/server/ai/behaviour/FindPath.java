package server.ai.behaviour;

import server.ai.Intel;
import server.ai.OrbBrain;
import server.game.Vector2;

import java.util.ArrayList;

/**
 * Defines a behaviour where the entity maps a path to the target.
 * Created by rhys on 2/16/17.
 */
public class FindPath extends Task {

    public FindPath(Intel intel, OrbBrain brain) {
        super(intel, brain);
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
        System.out.println("Finding Path.");
        ArrayList<Vector2> path = new ArrayList<>();
        path.add(intel.getTargetLocation());
        intel.resetPath(path);
        end();
    }


}
