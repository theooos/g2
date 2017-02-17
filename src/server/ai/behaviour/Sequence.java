package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Created by rhys on 2/16/17.
 */
public class Sequence extends Planner {

    public Sequence(Intel intel)
    {
        super(intel);
    }

    /**
     * Bails from the sequence when a sub-task fails.
     */
    @Override
    public void subTaskFailed()
    {
        control.fail();
    }


    /**
     * Moves onto the next sub-task in the sequence when a sub-task succeeds.
     */
    @Override
    public void subTaskSucceeded() {
        int curPos =
                control.subTasks.indexOf(control.curTask);
        if (curPos == (control.subTasks.size() - 1)) {
            control.succeed();
        }
        else {
            control.curTask = control.subTasks.elementAt(curPos+1);
            System.out.println("Checking conditions.");
            if(!control.curTask.checkConditions()) {
                control.fail();
            }
        }
    }
}
