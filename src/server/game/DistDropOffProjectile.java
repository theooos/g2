package server.game;

/**
 * Created by peran on 01/02/17.
 * Used so damage and radius drop off over range.
 */
public class DistDropOffProjectile extends Projectile {

    private float damageDec;
    private float radiusDec;

    /**
     * An example of projectile with a special feature - radius is a function of damage and both drop off over distance
     * @param damage the damage at birth
     * @param lifespan the lifespan before the prj dies
     * @param radius the radius of the prj at the start
     * @param pos the starting pos of the prj
     * @param dir the starting direction of the prj
     * @param speed the speed of the prj
     * @param phase the phase the prj is in
     */
    DistDropOffProjectile(int damage, int lifespan, int radius, Vector2 pos,
                          Vector2 dir, float speed, int phase, MovableEntity player, int ID) {
        super(damage, lifespan, radius, pos, dir, speed, phase, player, ID);
        damageDec = damage/lifespan;
        radiusDec = radius/lifespan;
    }

    private DistDropOffProjectile(DistDropOffProjectile d) {
        super(d.getDamage(), d.lifespan, d.getRadius(), d.pos, d.dir, d.speed, d.phase, d.getPlayer(), d.ID);
        damageDec = damage/lifespan;
        radiusDec = radius/lifespan;
    }

    @Override
    protected void live() {
        damage -= damageDec;
        radius -= radiusDec;
        super.live();
    }

    public DistDropOffProjectile clone() {
        return new DistDropOffProjectile(this);
    }
}
