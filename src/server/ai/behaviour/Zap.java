package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;
import server.game.MovableEntity;
import server.game.Orb;
import server.game.Player;

/**
 * Allows the entity to perform a very powerful, touching-range attack.
 * Restricted for use by Orbs only.
 * Created by rhys on 2/16/17.
 */
public class Zap extends Task {

    private final double FREQUENCY = 7;
    private double ctr;
    private MovableEntity target;

    public Zap(Intel intel, AIBrain brain) {
        super(intel, brain);
        this.ctr = 0;
    }

    @Override
    public boolean checkConditions() {
        assert (intel.ent() instanceof Orb);
        float distBetweenCent = intel.ent().getPos().getDistanceTo(intel.getRelevantEntity().getPos());
        float absDistBetween = distBetweenCent - intel.ent().getRadius();
        return (absDistBetween <= 40);
    }

    public void start(){
        super.start();
        this.target = intel.getRelevantEntity();
    }

    @Override
    public void doAction() {
        if (target.isAlive()){
            if (ctr == FREQUENCY) {
                target.damage(24);
                ctr = 0;
            } else {
                ctr++;
            }
        } else {
            end();
        }

    }
}
