package server.ai.orb;

import server.ai.Behaviour;
import server.game.Game;
import server.game.Player;
import server.game.Zombie;

import java.util.ArrayList;

/**
 * Allows an orb to move towards and attack a targeted player.
 * Created by rhys on 2/14/17.
 */
public class Stalk extends Behaviour {

    protected final ArrayList<Player> players;

    public Stalk(ArrayList<Player> players){
        super();
        this.players = players;
    }

    @Override
    public void reset() {

    }

    @Override
    public void act(Zombie orb, Game env) {

    }
}
