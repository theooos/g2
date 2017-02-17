package server.ai.behaviour;

import server.ai.Intel;

/**
 * Inner node of behaviour tree, allowing tasks to be assembled like
 * building blocks.
 * Created by rhys on 2/16/17.
 */
public abstract class Planner extends Behaviour {

    PlannerController control;

    public Planner(Intel intel){
        super(intel);
        createController();
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
            System.out.println("Starting un-started process.");
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
        control.curTask = control.subTasks.firstElement();
        if (control.curTask == null) {}
    }
}
