package server.game;

public class Projectile extends MovableEntity {

    float damage;
    int lifespan;
    private MovableEntity p;

    /**
     * A boring default projectile
     * @param damage the damage at birth
     * @param lifespan the lifespan before the prj dies
     * @param radius the radius of the prj at the start
     * @param pos the starting pos of the prj
     * @param dir the starting direction of the prj
     * @param speed the speed of the prj
     * @param phase the phase the prj is in
     */
    public Projectile(int damage, int lifespan, int radius, Vector2 pos, Vector2 dir, float speed, int phase, MovableEntity p, int id) {
        this.health = 10;
        this.damage = damage;
        this.lifespan = lifespan;
        this.damageable = false;
        this.visible = true;
        this.radius = radius;
        this.pos = pos;
        this.dir = dir;
        this.speed = speed;
        this.phase = phase;
        this.p = p;
        try {
            this.team = p.getTeam();
        }
        catch (NullPointerException e) {
            this.team = 2;
        }

        ID = id;
    }

    /**
     * A constructor to copy an existing projectile
     * @param p the projectile to copy
     */
    private Projectile(Projectile p) {
        this.health = 10;
        this.damage = p.damage;
        this.lifespan = p.lifespan;
        this.damageable = false;
        this.visible = true;
        this.radius = p.radius;
        this.pos = p.pos;
        this.dir = p.dir;
        this.speed = p.speed;
        this.phase = p.phase;
        this.p = p.getPlayer();
        try {
            this.team = p.getTeam();
        }
        catch (NullPointerException e) {
            this.team = 2;
        }

        ID = p.ID;
    }

    /**
     * Moves the projectile and ticks it life
     */
    void live() {
        move();
        tickLife();
    }

    public int getDamage() {
        return (int) damage;
    }

    private void tickLife() {
        lifespan--;
        if (lifespan < 1) {
            setHealth(0);
        }
    }

    /**
     * sets the lifespan of a proj to 0
     */
    void kill() {
        lifespan = 0;
        setHealth(0);
    }

    int getPlayerID() {
        return p.getID();
    }

    public MovableEntity getPlayer() {
        return p;
    }

    void setPlayer(Player p) {
        this.p = p;
    }

    void setTeam(int team) {
        this.team = team;
    }

    public Projectile clone() {
        return new Projectile(this);
    }

}
