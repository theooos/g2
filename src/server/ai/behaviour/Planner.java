package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Inner node of behaviour tree, allowing tasks to be assembled like
 * building blocks.
 * Created by rhys on 2/16/17.
 */
public abstract class Planner extends Behaviour {

    PlannerController control;

    public Planner(Intel env, MovableEntity ent){
        super(env, ent);
        createController();
    }

    private void createController() {
        this.control = new PlannerController(this);
    }

    public TaskController getControl(){
        return control;
    }

    public boolean checkConditions() {
        return control.subtasks.size() > 0;
    }

    public abstract void subTaskSucceeded();

    public abstract void subTaskFailed();

    public void DoAction() {
        if (control.finished() || control.curTask == null) {
            return;
        }

        if (!control.curTask.getControl().started()) {
            control.curTask.getControl().safeStart();
        } else if (control.curTask.getControl().finished()) {
            control.curTask.getControl.safeEnd();
            if (control.curTask.getControl.succeeded()){
                this.subTaskSucceeded();
            }
            if (control.curTaskgetControl.failed()) {
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
        control.curTask = control.subtasks.firstElement();
        if (control.curTask == null) {}
    }
}
