package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/23/17.
 * Testing weapons
 */
class WeaponTest {
    Weapon shotgun;
    Weapon sniper;
    Weapon smg;

    @BeforeEach
    void setUp() {
         shotgun = new WeaponShotgun();
         sniper = new WeaponSniper();
         smg = new WeaponSMG();
    }

    @Test
    void live() {

    }

    @Test
    void canFire() {

    }

    @Test
    void getShots() {

    }

    @Test
    void isFullyAuto() {
        assertTrue(!shotgun.isFullyAuto());
        assertTrue(smg.isFullyAuto());
    }

    @Test
    void toStringTest() {
        assertTrue(shotgun.toString().equals("shotgun"));
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