package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/23/17.
 * Testing weapons
 */
class WeaponTest {
    private Weapon shotgun;
    private Weapon sniper;
    private Weapon smg;

    @BeforeEach
    void setUp() {
         shotgun = new WeaponShotgun();
         sniper = new WeaponSniper();
         smg = new WeaponSMG();
    }

    @Test
    void live() {
        assertTrue(shotgun.getHeat() == 0);
        shotgun.setCurrentHeat(50);
        shotgun.live();
        assertTrue(shotgun.getHeat() < 50);
    }

    @Test
    void canFire() {
        assertTrue(shotgun.canFire());
        shotgun.setCurrentHeat(99);
        assertFalse(shotgun.canFire());
        shotgun.setCurrentHeat(40);
        shotgun.getShots(new Player(new Vector2(0, 0), new Vector2(1, 0), 0, 0, null, null, 0));
        assertFalse(shotgun.canFire());
    }

    @Test
    void getShots() {
        ArrayList x = shotgun.getShots(new Player(new Vector2(0, 0), new Vector2(1, 0), 0, 0, null, null, 0));
        assertTrue(x.size() == 7);
        x = sniper.getShots(new Player(new Vector2(0, 0), new Vector2(1, 0), 0, 0, null, null, 0));
        assertTrue(x.size() == 1);
    }

    @Test
    void isFullyAuto() {
        assertTrue(!shotgun.isFullyAuto());
        assertTrue(smg.isFullyAuto());
    }

    @Test
    void toStringTest() {
        assertTrue(shotgun.toString().equals("Shotgun"));
    }

    @Test
    void getHeat() {
        assertTrue(shotgun.getHeat() == 0);
    }

    @Test
    void getMaxHeat() {
        assertTrue(shotgun.getMaxHeat() == 120);
    }

    @Test
    void getRefireTime() {
        assertTrue(shotgun.getRefireTime() == 18);
        assertTrue(sniper.getRefireTime() == 25);
        assertTrue(smg.getRefireTime() == 10);
    }

    @Test
    void setCurrentHeat() {
        shotgun.setCurrentHeat(30);
        assertTrue(shotgun.getHeat() == 30);
    }

}