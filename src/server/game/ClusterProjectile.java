package server.game;

import java.util.ArrayList;

/**
 * Created by peran on 01/02/17.
 */
public class ClusterProjectile extends Projectile {

    private boolean clustered;
    private ArrayList<Projectile> children;
    private int numChildren;

    /**
     * An example of projectile with a special feature - when its lifespan ends it explodes into 4 smaller projectiles
     * @param damage the damage at birth
     * @param lifespan the lifespan before the prj dies
     * @param radius the radius of the prj at the start
     * @param pos the starting pos of the prj
     * @param dir the starting direction of the prj
     * @param speed the speed of the prj
     * @param phase the phase the prj is in
     */
    public ClusterProjectile(int damage, int lifespan, int radius, Vector2 pos, Vector2 dir, float speed, int phase) {
        super(damage, lifespan, radius, pos, dir, speed, phase);
        clustered = false;
        children = new ArrayList<Projectile>();
    }

    @Override
    protected void tickLife() {
        if (!clustered) {
            lifespan--;
            if (lifespan < 1) {
                cluster();
                clustered = true;
                setVisible(false);
            }
        }
        else {
            if (children.isEmpty()) {
                setHealth(0);
            }
            else {
                for (Projectile p: children) {
                    if (!p.isAlive()) {
                        children.remove(p);
                    }
                }
            }
        }

    }

    private void cluster() {
        int newDam = damage/4;
        int newRad = radius/4;
        int newlife = 50;
        children.add(new Projectile(newDam, newlife, newRad, pos, dir.add(new Vector2(0, 1)), speed, phase));
        children.add(new Projectile(newDam, newlife, newRad, pos, dir.add(new Vector2(1, 0)), speed, phase));
        children.add(new Projectile(newDam, newlife, newRad, pos, dir.add(new Vector2(0, -1)), speed, phase));
        children.add(new Projectile(newDam, newlife, newRad, pos, dir.add(new Vector2(-1, 0)), speed, phase));

    }


}
