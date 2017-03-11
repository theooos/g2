package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.game.Orb;
import server.game.Vector2;

import java.util.Random;

/**
 * Created by rhys on 3/5/17.
 */
public class Dawdle extends Task {

    private Random gen;
    private int counter;
    private int timer;
    private Vector2 returnPos;
    private final double INIT_PROB = 0.95;

    public Dawdle(Intel intel, AIBrain brain) {
        super(intel, brain);
        this.gen = new Random();
        this.counter = 0;
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
        if (gen.nextDouble() >= INIT_PROB){
            timer = 0;
            counter = 0;
            end();
        } else {
            counter += 0.0001;
        }
    }

    public void run(){
        System.err.println("Dawdle is not a single-tick task.");
        System.exit(1);
    }
}
