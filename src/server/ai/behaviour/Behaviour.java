package server.ai.behaviour;

import server.ai.Intel;
import server.game.Game;
import server.game.MovableEntity;
import server.game.Orb;

/**
 * Abstract Behaviour object allowing construction of Behaviour tree.
 * Based on tutorial at https://goo.gl/0f9F0U .
 * Created by rhys on 2/14/17.
 */
public abstract class Behaviour {

    protected Intel env;
    protected MovableEntity ent;

    /**
     * Constructor for Abstract class.
     */
    protected Behaviour(Intel env, MovableEntity ent){
        this.env = env;
        this.ent = ent;
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
