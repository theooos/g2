package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used to test players and a bunch of movable entity classes
 */
class PlayerTest {
    Player p;
    @BeforeEach
    void setUp() {
        p = new Player(new Vector2(1, 1), new Vector2(1, 0), 1, 1, new WeaponShotgun(), new WeaponSniper(), 0);
        p = new Player(p);
    }

    @Test
    void live() {
        p.setFiring(true);
        p.setHealth(0);
        p.live();
        assertFalse(p.isFiring());
    }


    @Test
    void getActiveWeapon() {
        assertTrue(p.getActiveWeapon().equals(new WeaponShotgun()));
    }

    @Test
    void isWeaponOneOut() {
        assertTrue(p.isWeaponOneOut());
    }

    @Test
    void setWeaponOut() {
        p.setWeaponOut(false);
        assertFalse(p.isWeaponOneOut());
    }

    @Test
    void togglePhase() {
        int phase = p.getPhase();
        p.togglePhase();
        assertTrue(p.getPhase() != phase);
    }

    @Test
    void isFiring() {
        assertFalse(p.isFiring());
    }

    @Test
    void setFiring() {
        p.setFiring(true);
        assertTrue(p.isFiring());
    }

    @Test
    void getWeaponOutHeat() {
        assertTrue(p.getWeaponOutHeat() == 0);
    }

    @Test
    void setWeaponOutHeat() {
        p.setWeaponOutHeat(4);
        assertTrue(p.getWeaponOutHeat() == 4);
    }

    @Test
    void getMoveCount() {
        assertTrue(p.getMoveCount() == 0);
    }

    @Test
    void incMove() {
        p.incMove();
        assertTrue(p.getMoveCount() == 1);
    }

    @Test
    void setMoveCount() {
        p.setMoveCount(2);
        assertTrue(p.getMoveCount() == 2);
    }

    @Test
    void getPhasePercentage() {
        assertTrue(p.getPhasePercentage() == 1);
    }

    @Test
    void setPhasePercentage() {
        p.setPhasePercentage(3);
        assertTrue(p.getPhasePercentage() == 3);
    }

    @Test
    void move1() {
        p.move();
        assertTrue(p.getPos().equals(new Vector2(1, 1).add(p.getDir().mult(p.getSpeed()))));
    }

    @Test
    void getSpeed() {
        assertTrue(p.getSpeed() == 5);
    }

    @Test
    void setSpeed() {
        p.setSpeed(3);
        assertTrue(p.getSpeed() == 3);
    }

    @Test
    void getDir() {
        assertTrue(p.getDir().equals(new Vector2(1,0)));
    }

    @Test
    void setDir() {
        p.setDir(new Vector2(0, 1));
        assertTrue(p.getDir().equals(new Vector2(0,1)));
    }

    @Test
    void setRadius() {
        p.setRadius(6);
        assertTrue(p.getRadius() == 6);
    }

    @Test
    void getRadius() {
        assertTrue(p.getRadius() == 20);
    }

    @Test
    void getTeam() {
        assertTrue(p.getTeam() == 1);
    }

    @Test
    void canRespawn() {
        assertFalse(p.canRespawn());
    }

    @Test
    void resetTimeTillRespawn() {
        p.resetTimeTillRespawn();
        assertFalse(p.canRespawn());
    }

}