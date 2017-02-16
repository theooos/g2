package server.ai.behaviour;

import java.util.Vector;

/**
 * Controls super-tasks.
 * Created by rhys on 2/16/17.
 */
public class PlannerController extends TaskController {

    public Vector<Behaviour> subTasks;
    public Behaviour curTask;

    public PlannerController(Behaviour behaviour) {
        super(behaviour);
        this.subTasks = new Vector<Behaviour>();
        this.curTask = null;
    }

    public void add(Behaviour behaviour){
        subTasks.add(behaviour);
    }

    public void reset() {
        super.reset();
        this.curTask = subTasks.firstElement();
    }


}
