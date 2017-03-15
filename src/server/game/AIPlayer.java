package server.game;

/**
 * Created by peran on 01/02/17.
 * THe players which rhys' AI will control
 */
public class AIPlayer extends Player {

    public AIPlayer(Vector2 pos, Vector2 dir, int team, int phase, Weapon w1, Weapon w2, int id) {
        super(pos, dir, team, phase, w1, w2, id);
    }

    public void live() {
        super.live();
        move();
        //any other methods the ai may do once a tick
    }

    public void move() {
        //movement based on more advanced ai code
    }

}
