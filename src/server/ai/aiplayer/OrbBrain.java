package server.ai.aiplayer;

import server.ai.Intel;
import server.ai.behaviour.*;

/**
 * Created by rhys on 2/20/17.
 */
public class OrbBrain {

    public enum emotionalState{SCARED, ANGRY, RELAXED};
    private emotionalState emotion;

    private Intel intel;
    private Feel feel;
    private LocateCover coverLocator;
    private FindPath pathfinder;
    private Travel traveller;
    private Zap zapper;
    private Wander wanderer;

    public OrbBrain(Intel intel) {
        this.intel = intel;
        constructBehaviours();
    }

    private void constructBehaviours(){

        this.feel = new Feel(intel);
        this.coverLocator = new LocateCover(intel);
        this.pathfinder = new FindPath(intel);
        this.traveller = new Travel(intel);
        this.zapper = new Zap(intel);
        this.wanderer = new Wander(intel);
    }

    public void doSomething(){
        this.emotionalState = feel();

    }
}
