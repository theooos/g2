package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;

import java.util.ArrayList;

/**
 * Allows control of a sequence of behaviours.
 *
 * Created by Rhys on 2/16/17.
 */
public class Sequence extends Task {

    private ArrayList<Task> subTasks;
    private Task curTask;

    /**
     * Constructs an empty behaviour sequence.
     *
     * @param intel the intelligence object the behaviours will use to carry
     *              out their decisions and actions.
     * @param brain the brain that will exhibit the behaviours stored within
     *              this sequence.
     */
    public Sequence(Intel intel, AIBrain brain) {
        super(intel, brain);
        this.subTasks = new ArrayList<>();
        this.curTask = null;
    }

    /**
     * Appends a behaviour to this sequence.
     *
     * @param newTask the behaviour to be added.
     */
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

    @Override
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
     * Moves onto the next sub-task in this sequence when a sub-task succeeds.
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

    /**
     * Resets progress of all behaviours within this sequence, as well as the overall
     * progress of this sequence itself.
     */
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

    /**
     * Checks to see if the final sub-task in this sequence has finished, which
     * determines whether or not this sequence as a whole has finished.
     *
     * @return <CODE>true</CODE> if the sequence has finished.
     */
    @Override
    public boolean hasFinished(){
        return (subTasks.get(subTasks.size()-1)).hasFinished();
    }
}
