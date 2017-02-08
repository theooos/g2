package server.game;

import java.util.HashSet;

/**
 * Represents one wall of a game map.
 * Created by rhys on 2/1/17.
 */
public class Wall extends Entity {

    private Vector2 endPos;

    /**
     * Builds a wall based on the given parameters.
     * @param startPos the vector position of one end of the wall.
     * @param endPos the vector position of the other end of the wall.
     * @param phase the phase the wall belongs to.
     * @param damageable
     */
    public Wall(Vector2 startPos, Vector2 endPos, int phase, boolean damageable){

        this.pos = startPos;
        this.endPos = endPos;
        this.phase = phase;
        this.damageable = damageable;
        this.health = 100;
        this.visible = true;
    }


    /**
     * @return a vector2 object representing the wall's start position.
     */
    public Vector2 getStartPos() {
        return getPos();
    }


    /**
     * @return a vector2 object representing the wall's end position.
     */
    public Vector2 getEndPos() {
        return endPos;
    }


    /**
     * Deals damage to this wall (if the wall is damageable).
     * @param hp - The number of hit points-worth of damage to be dealt.
     * @return true if this wall is destroyed.
     */
    public boolean damageWall(int hp){

        if (damageable) {
            int newHealth = health - hp;
            if (newHealth <= 0) {
                health = 0;
                visible = false;
                return true;
            } else {
                health = newHealth;
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * Returns true if this wall is not broken. More formally, returns true if and only if health > 0.
     * @return true if this wall is not broken.
     */
    public boolean intact(){
        return isAlive();
    }


    /**
     * Returns true if this wall is in the given phase.
     * @param phase - int representing the phase for which this wall is to be tested.
     * @return true if this wall is in the given phase.
     */
    public boolean inPhase(int phase){
        return (this.phase == phase);
    }

    public HashSet<Vector2> getWholeWall(){
        return null;
    }

}