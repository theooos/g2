package server.ai.behaviour;

import server.ai.Intel;
import testbed.AITestUI;

/**
 * Inner node of behaviour tree, allowing tasks to be assembled like
 * building blocks.
 * Created by rhys on 2/16/17.
 */
public abstract class Planner extends Behaviour {

    PlannerController control;
    String name;

    public Planner(Intel intel, String name){
        super(intel);
        createController();
        this.name = name;
    }

    private void createController() {
        this.control = new PlannerController(this);
    }

    public TaskController getControl(){
        return control;
    }

    public boolean checkConditions() {
        return control.subTasks.size() > 0;
    }

    public abstract void subTaskSucceeded();

    public abstract void subTaskFailed();

    public void doAction() {

        // If the super-task has finished or we have null sub-task:
        if (control.finished() || control.curTask == null) {
            return;
        }

        // If we do have a current task:
        if (!control.curTask.getControl().started()) {
            control.curTask.getControl().safeStart(); // Start it if it isn't already started.
        } else if (control.curTask.getControl().finished()) {
            control.curTask.getControl().safeEnd();
            if (control.curTask.getControl().succeeded()){
                this.subTaskSucceeded();
            }
            if (control.curTask.getControl().failed()) {
                this.subTaskFailed();
            }
        }
        else {
            control.curTask.doAction();
        }
    }

    public void end() {

    }

    public void start() {
        if (AITestUI.DEBUG) System.out.println(name + " starting.");
        control.curTask = control.subTasks.firstElement();
    }
}
