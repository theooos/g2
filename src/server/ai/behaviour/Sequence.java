package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Created by rhys on 2/16/17.
 */
public class Sequence extends Planner {

    public Sequence(Intel env, MovableEntity ent)
    {
        super(env, ent);
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
    public void subTaskSucceeded()
    {
        int curPos =
                control.subtasks.indexOf(control.curTask);
        if( curPos ==
                (control.subTasks.size() - 1))
        {
            control.succeed();
        }
        else
        {
            control.curTask = control.subtasks.elementAt(curPos+1);
            if(!control.curTask.CheckConditions())
            {
                control.failure();
            }
        }
    }
}
