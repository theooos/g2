package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Created by rhys on 2/16/17.
 */
public class Selector extends Planner {

    public Selector(Intel intel) {
        super(intel);
    }

    @Override
    public void subTaskSucceeded() {
        control.succeed();
    }

    @Override
    public void subTaskFailed() {
        control.curTask = chooseNewTask();
        if(control.curTask == null) {
            control.fail();
        }
    }

    public Behaviour chooseNewTask() {
        System.out.println("Choosing task...");
        Behaviour behaviour = null;
        boolean found = false;
        int curPos = control.subTasks.indexOf(control.curTask);

        while (!found){
            if (curPos == (control.subTasks.size() - 1)) {
                found = true;
                behaviour = null;
                break;
            }

            curPos++;
            behaviour = control.subTasks.elementAt((curPos));

            System.out.println("Checking conditions: Selector");
            if (behaviour.checkConditions()) {
                found = true;
            }
        }

        return behaviour;
    }


}
