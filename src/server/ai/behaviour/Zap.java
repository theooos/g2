package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;
import server.game.MovableEntity;
import server.game.Orb;
import server.game.Player;

/**
 * This behaviour allows an Orb to perform a very powerful attack upon
 * players in touching range.
 *
 * Created by Rhys on 2/16/17.
 */
public class Zap extends Task {

    private final double FREQUENCY = 7;
    private double ctr;
    private MovableEntity target;

    /**
     * Constructs a Zap behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel the game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain the brain of the AI player that will be exhibiting this behaviour.
     */
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

    @Override
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
