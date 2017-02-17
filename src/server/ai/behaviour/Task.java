package server.ai.behaviour;

import server.ai.Intel;

/**
 * Represents a leaf task of the Behaviour Tree.
 * Created by rhys on 2/16/17.
 */
public abstract class Task extends Behaviour {

    protected TaskController control;   // Keeps track of Task state.

    public Task(Intel intel){
        super(intel);
        createController();
    }

    /**
     * Creates the controller for this task.
     */
    private void createController() {
        this.control = new TaskController(this);
    }

    /**
     * @return this task's controller.
     */
    public TaskController getControl(){
        return this.control;
    }


}
