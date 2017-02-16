package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Searches the nearby area for players.
 * Created by rhys on 2/16/17.
 */
public class LookAround extends Task {

    public LookAround(Intel env, MovableEntity ent){
        super(env, ent);
    }

    @Override
    public boolean checkConditions() {
        return ent.isAlive();
    }

    @Override
    public void doAction() {
        // Code HERE for checking for nearby players.
        // PERQUISITE: Collision Detection.

        env.setPlayerNearby(true);
        getControl().succeed();
    }

    @Override
    public void end() {
        System.out.println("Ending task: LOOK AROUND.");
    }

    @Override
    public void start() {
        System.out.println("Starting task: LOOK AROUND.");
    }


}
