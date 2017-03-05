package server.ai.behaviour;

import server.ai.Intel;
import server.ai.OrbBrain;

import java.util.ArrayList;

/**
 * Allows control of a sequence of behaviours.
 * Created by rhys on 2/16/17.
 */
public class Sequence extends Task {

    private ArrayList<Task> subTasks;
    private Task curTask;

    public Sequence(Intel intel, OrbBrain brain) {
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
        System.out.println("Starting Sequence.");
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
            System.out.println("Continuing with sub-task.");
            curTask.doAction();
        }   //...but it's not running yet:
        else if (curTask.isDormant()) {
            System.out.println("Starting sub-task.");
            curTask.start();
        }   //...but it's already finished:
        else if (curTask.hasFinished()) {
            System.out.println("Selecting next sub-task.");
            advanceSequence();
        } else {
            System.out.println("No conditions met.");
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
            System.out.println("Repeating sequence.");
            reset();
        }
        // Otherwise, move on.
        else {
            curTask = subTasks.get(curPos+1);
            if(!curTask.checkConditions()) {
                end();
            } else {
            }
        }
    }

    public void reset() {
        System.out.println("Resetting Sequence.");
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
