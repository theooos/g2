package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;

import java.util.ArrayList;

/**
 * Allows control of a sequence of behaviours.
 * Created by rhys on 2/16/17.
 */
public class Sequence extends Task {

    protected ArrayList<Task> subTasks;
    protected Task curTask;

    public Sequence(Intel intel, AIBrain brain) {
        super(intel, brain);
        this.subTasks = new ArrayList<>();
        this.curTask = null;
    }

    public void add(Task newTask){
        subTasks.add(newTask);
    }

    @Override
    public boolean checkConditions() {
        return (subTasks.size() > 0);
    }

    @Override
    public void start(){
        curTask = subTasks.get(0);
        if (curTask.checkConditions()) {
            curTask.start();
        } else end();

    }

    public void doAction() {
        // If the super-task has finished or we have a null sub-task:
        if (hasFinished() || curTask == null) {
            end();
            return;
        }

        // If we DO have a current task...
            //...and it's running.
        if (curTask.isRunning()) {
            curTask.doAction();
        }   //...but it's not running yet:
        else if (curTask.isDormant()) {
            curTask.start();
        }   //...but it's already finished:
        else if (curTask.hasFinished()) {
            advanceSequence();
        }
    }

    /**
     * Moves onto the next sub-task in the sequence when a sub-task succeeds.
     */
    private void advanceSequence() {
        int curPos = subTasks.indexOf(curTask);

        // If we've reached the end of the sequence, the sequence is successful.
        if (curPos == (subTasks.size() - 1)) {
            // Reset the sequence, so that it can be repeated as many times as necessary.
            reset();
        }
        // Otherwise, move on.
        else {
            curTask = subTasks.get(curPos+1);
            if(!curTask.checkConditions()) {
                end();
            }
        }
    }

    public void reset() {
        super.reset();
        this.curTask = subTasks.get(0);
        for (Task task : subTasks) {
            task.reset();
        }
    }

    @Override
    public void run(){
        System.err.println("Sequences are not single-tick tasks.");
        System.exit(1);
    }
}
