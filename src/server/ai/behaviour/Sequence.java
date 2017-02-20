package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;
import testbed.AITestUI;

/**
 * Allows control of a sequence of behaviours.
 * Created by rhys on 2/16/17.
 */
public class Sequence extends Planner {

    public Sequence(Intel intel, String name)
    {
        super(intel, name);
    }

    /**
     * Bails from the sequence when a sub-task fails.
     */
    @Override
    public void subTaskFailed()
    {
        control.fail();
    }

    @Override
    public void start(){
        control.curTask = control.subTasks.firstElement();
        if (control.curTask.checkConditions()) {
            control.curTask.getControl().safeStart();
        } else control.fail();

    }

    /**
     * Moves onto the next sub-task in the sequence when a sub-task succeeds.
     */
    @Override
    public void subTaskSucceeded() {
        if (AITestUI.DEBUG) System.out.println(control.curTask.toString() + " succeeded. Moving on...");
        int curPos = control.subTasks.indexOf(control.curTask);

        // If we've reached the end of the sequence, the sequence is successful.
        if (curPos == (control.subTasks.size() - 1)) {
            control.succeed();
        }
        // Otherwise, move on.
        else {
            control.curTask = control.subTasks.elementAt(curPos+1);
            if(!control.curTask.checkConditions()) {
                control.fail();
            }
        }
    }
}
