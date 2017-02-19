package server.ai.behaviour;

import server.ai.Intel;
import testbed.AITestUI;

/**
 * Represents a leaf task of the Behaviour Tree.
 * Created by rhys on 2/16/17.
 */
public abstract class Task extends Behaviour {

    private TaskController control;   // Keeps track of Task state.
    String name;

    public Task(Intel intel, String name) {
        super(intel);
        this.control = new TaskController(this);
        this.name = name;
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

    @Override
    public void start() {
        if (AITestUI.DEBUG) System.out.println("Starting Task: " + name + ".");
    }

    @Override
    public void end() {
        if (AITestUI.DEBUG) System.out.println("Ending Task: " + name + ".");
    }


}
