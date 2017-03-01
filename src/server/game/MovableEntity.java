package server.game;

/**
 * Created by peran on 27/01/17.
 */
public class MovableEntity extends Entity {
    protected float speed;
    protected Vector2 dir;
    protected int radius;
    protected int team;

    /**
     * A class intended for inheritence, should not be created
     */
    public MovableEntity() {
        super();
    }

    protected void move() {
        this.pos = pos.add(dir.mult(speed));

    }

    public Vector2 hypoMove() {
        return pos.add(dir.mult(speed));
    }

    public float getSpeed() {
        return speed;
    }

    public Vector2 getDir() {
        return  dir;
    }

    public void setDir(Vector2 dir) {
        this.dir = dir;
    }

    public int getRadius() {
        return radius;
    }

    public int getTeam() {
        return team;
    }
}
