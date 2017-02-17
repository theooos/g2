package server.ai.behaviour;

import server.ai.Intel;

/**
 * Abstract Behaviour object allowing construction of Behaviour tree.
 * Based on tutorial at https://goo.gl/0f9F0U .
 * Created by rhys on 2/14/17.
 */
public abstract class Behaviour {

    protected Intel intel;

    /**
     * Constructor for Abstract class.
     * @param intel
     */
    protected Behaviour(Intel intel){
        this.intel = intel;
    }

    /**
     * Check whether or not the behaviour's initial conditions are met.
     * @return true if the behaviour is permissible.
     */
    public abstract boolean checkConditions();

    /**
     * Performs the startup logic of the behaviour.
     */
    public abstract void start();

    /**
     * Performs the ending logic of the behaviour.
     */
    public abstract void end();

    /**
     * Performs the updating logic the behaviour must perform each cycle.
     */
    public abstract void doAction();


    public abstract TaskController getControl();

}
