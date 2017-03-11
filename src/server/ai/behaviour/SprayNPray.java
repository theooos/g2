package server.ai.behaviour;

import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * Created by rhys on 3/11/17.
 */
public class SprayNPray extends Sequence {

    private PlayerIntel intel;
    private PlayerBrain brain;

    public SprayNPray(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
        this.intel = intel;
        this.brain = brain;

        this.subTasks.add(brain.getBehaviour("FindPath"));
        this.subTasks.add(brain.getBehaviour("Travel"));
        this.subTasks.add(brain.getBehaviour("Fire"));
    }


}
