package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;
import server.game.Orb;
import server.game.Vector2;

import java.util.Random;

/**
 * This behaviour allows an Orb to hover in place with a vibration-like
 * effect for an undetermined amount of time.
 *
 * Created by Rhys on 3/5/17.
 */
public class Dawdle extends Task {

    private Random gen;
    private int timer;
    private Vector2 returnPos;
    private final double INIT_PROB = 0.95;

    /**
     * Constructs a Dawdle behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel the game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain the brain of the AI player that will be exhibiting this behaviour.
     */
    public Dawdle(Intel intel, AIBrain brain) {
        super(intel, brain);
        this.gen = new Random();
        this.timer = 0;
    }

    @Override
    public boolean checkConditions(){
        return (intel.ent().isAlive());
    }

    @Override
    public void doAction(){
        Orb orb = (Orb) intel.ent();

        if (timer % 8 == 0){
            // Make Orb move in a random direction.
            this.returnPos = orb.getPos();
            orb.setDir(new Vector2(gen.nextFloat(), gen.nextFloat()));
            orb.setPos(orb.getPos().add(orb.getDir().mult(orb.getSpeed())));
        }
        else if (timer % 8 == 4) {
            orb.setPos(returnPos);
        }
        timer++;


        // Decide whether or not the Orb is finished dawdling.
        if (gen.nextDouble() >= INIT_PROB) {
            timer = 0;
            end();
        }
    }

    /**
     * Prevents this behaviour from being run as a single-tick-task.
     */
    @Override
    public void run(){
        System.err.println("Dawdle is not a single-tick task.");
        System.exit(1);
    }
}
