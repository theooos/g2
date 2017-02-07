package server.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a game map.
 * Created by rhys on 2/2/17.
 */
public class Map {

    private ArrayList<Wall> walls;
    private int width;
    private int length;
    private final String LOCAL_PATH = "/home/rhys/Dropbox/University/Year2-Semester2/TeamProject/";
    private final String PROJ_PATH = "g2/src/server/game/maps/";
    private final String FULL_PATH = LOCAL_PATH + PROJ_PATH + "map";

    /**
     * Generates the requested map from the appropriate text file.
     * @param mapID
     */
    public Map(int mapID) throws IOException {

        walls = new ArrayList<>();
        ArrayList<String> wallStrings = new ArrayList<>();

        // Read the map's text file.
        try {
            FileReader file = new FileReader(FULL_PATH + String.valueOf(mapID) + ".txt");
            BufferedReader input = new BufferedReader(file);
            String nextWallString;
            while ((nextWallString = input.readLine()) != null) {
                wallStrings.add(nextWallString);
            }
            input.close();
        } catch (IOException e) {
            throw new IOException();
        }

        // Parse the overall dimensions of the map.
        List<String> dimensions = Arrays.asList(wallStrings.get(0).split("\\s*,\\s*"));
        width = Integer.parseInt(dimensions.get(0));
        length = Integer.parseInt(dimensions.get(1));
        wallStrings.remove(0);

        // Parse each string and build a wall from it.
        for (String wall : wallStrings) {
            List<String> items = Arrays.asList(wall.split("\\s*,\\s*"));
            int x1 = Integer.parseInt(items.get(0));
            int y1 = Integer.parseInt(items.get(1));
            Vector2 startPos = new Vector2(x1, y1);

            int x2 = Integer.parseInt(items.get(2));
            int y2 = Integer.parseInt(items.get(3));
            Vector2 endPos = new Vector2(x2, y2);

            int phase = Integer.parseInt(items.get(4));
            boolean damageable = Boolean.parseBoolean(items.get(5));

            walls.add(new Wall(startPos, endPos, phase, damageable));
        }
    }


    /**
     * Returns an ArrayList of walls that exist in the given phase.
     * @param phase - int representing the phase to test each wall against.
     * @param intactOnly - if true, restricts the resulting ArrayList to only walls that are intact.
     * @return an ArrayList of walls that exist in the given phase.
     */
    public ArrayList<Wall> wallsInPhase(int phase, boolean intactOnly){

        ArrayList<Wall> relWalls = new ArrayList<>();


        for (Wall thisWall : walls) {
            if (thisWall.inPhase(phase)){
                if (!(intactOnly && !thisWall.intact())) {
                    relWalls.add(thisWall);
                }
            }
        }

        return relWalls;
    }

    /**
     * @return the width of the map in position units.
     */
    public int getMapWidth() {
        return width;
    }

    /**
     * @return the length of the map in position units.
     */
    public int getMapLength() {
        return length;
    }
}
