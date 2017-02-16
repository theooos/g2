package server.ai.behaviour;

/**
 * Created by rhys on 2/16/17.
 */
public class TaskController {

    private boolean finished;
    private boolean success;
    private boolean started;
    private Behaviour behaviour;

    public TaskController(Behaviour behaviour){
        setBehaviour(behaviour);
        this.finished = false;
        this.success = true;
        this.started = false;
    }

    public void setBehaviour(Behaviour behaviour){
        this.behaviour = behaviour;
    }

    public void safeStart() {
        this.started = true;
        behaviour.start();
    }

    public void safeEnd() {
        this.finished = false;
        this.started = false;
        behaviour.end();
    }

    protected void succeed() {
        this.success = true;
        this.finished = true;
    }

    protected void fail() {
        this.success = false;
        this.finished = true;
    }

    public boolean succeeded() {
        return success;
    }

    public boolean failed() {
        return !success;
    }

    public boolean finished() {
        return finished;
    }

    public boolean started() {
        return started;
    }

    public void reset() {
        this.finished = false;
    }
}
