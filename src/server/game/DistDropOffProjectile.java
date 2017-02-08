package server.game;

/**
 * Created by peran on 01/02/17.
 */
public class DistDropOffProjectile extends Projectile {

    private int damageDec;
    private int radiusDec;

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
    public DistDropOffProjectile(int damage, int lifespan, int radius, Vector2 pos,
                                 Vector2 dir, float speed, int phase, Player player) {
        super(damage, lifespan, radius, pos, dir, speed, phase, player);
        damageDec = damage/lifespan;
        radiusDec = radius/lifespan;
    }

    @Override
    protected void tickLife() {
        super.tickLife();
        damage -= damageDec;
        radius -= radiusDec;
    }
}
