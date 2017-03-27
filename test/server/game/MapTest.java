package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.Sys;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/27/17.
 * Used to test the map class
 */
class MapTest {
    Map m;


    @BeforeEach
    void setUp() {
        m = new Map(10);
    }

    @Test
    void wallsInPhase() {
        ArrayList<Wall> walls = m.wallsInPhase(0, true, true);
      //  100, 300, 300, 300, 0, false
      //  500, 300, 700, 300, 0, false
        ArrayList<Wall> testing = new ArrayList<>();
        testing.add(new Wall(new Vector2(100, 300), new Vector2(300, 300), 0, false, false));
        testing.add(new Wall(new Vector2(500, 300), new Vector2(700, 300), 0, false, false));
        assertTrue(walls.equals(testing));

    }

    @Test
    void getMapCapacity() {
        assertTrue(m.getMapCapacity() == 4);
    }

    @Test
    void getMapWidth() {
        assertTrue(m.getMapWidth() == 800);
    }

    @Test
    void getMapLength() {
        assertTrue(m.getMapLength() == 600);
    }

    @Test
    void getWalls() {
        ArrayList<Wall> walls = m.getWalls();
        System.out.println(walls.size());
//
//        0,   0,   800, 0
//        0,   0,   0,   600
//        800, 0,   800, 600
//        0,   600, 800, 600
//        100, 300, 300, 300, 0, false
//        500, 300, 700, 300, 0, false
        ArrayList<Wall> testing = new ArrayList<>();

        testing.add(new Wall(new Vector2(0, 0), new Vector2(800, 0), 0, false, true));
        testing.add(new Wall(new Vector2(0, 0), new Vector2(800, 0), 1, false, true));

        testing.add(new Wall(new Vector2(0, 0), new Vector2(0, 600), 0, false, true));
        testing.add(new Wall(new Vector2(0, 0), new Vector2(0, 600), 1, false, true));


        testing.add(new Wall(new Vector2(800, 0), new Vector2(800, 600), 0, false, true));
        testing.add(new Wall(new Vector2(800, 0), new Vector2(800, 600), 1, false, true));


        testing.add(new Wall(new Vector2(0, 600), new Vector2(800, 600), 0, false, true));
        testing.add(new Wall(new Vector2(0, 600), new Vector2(800, 600), 1, false, true));


        testing.add(new Wall(new Vector2(100, 300), new Vector2(300, 300), 0, false, false));
        testing.add(new Wall(new Vector2(500, 300), new Vector2(700, 300), 0, false, false));
        assertTrue(walls.equals(testing));
    }

}