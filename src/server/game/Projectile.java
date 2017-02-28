package server.game;

public class Projectile extends MovableEntity {

    int damage;
    int lifespan;
    private Player p;

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
    public Projectile(int damage, int lifespan, int radius, Vector2 pos, Vector2 dir, float speed, int phase, Player p, int id) {
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
     * A constructor to copy an exsisting projectile
     * @param p the projectile to copy
     */
    public Projectile(Projectile p) {
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

    void live() {
        move();
        tickLife();
    }

    int getDamage() {
        return damage;
    }

    protected void tickLife() {
        lifespan--;
        if (lifespan < 1) {
            setHealth(0);
            System.out.println("killed due to lifespan");
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

    Player getPlayer() {
        return p;
    }

    void setPlayer(Player p) {
        this.p = p;
    }
}
