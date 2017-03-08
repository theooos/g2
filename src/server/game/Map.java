package server.game;

import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Represents a game map.
 * Created by rhys on 2/2/17.
 */
public class Map {

    // Game Object Information:
    private int mapID;
    private String mapName;
    private int mapCapacity;                        // Denotes this map's max number of players.
    private int numPhases;
    private ArrayList<Wall> walls;
    private ArrayList<SpawnPosition> spawnPositions;
    private int width;
    private int length;
    private String FULL_PATH;
    private static final boolean LOCAL = true;

    // Short-term members:
    private ArrayList<Vector2> validSpace;


    /**
     * Generates the requested map from the appropriate text file.
     * @param mapID
     */
    public Map(int mapID) throws IOException {

        String LOCAL_PATH = new File("").getAbsolutePath();
//        System.out.println(LOCAL_PATH);
        String PROJ_PATH;
        if (LOCAL) {
            PROJ_PATH = "/src/server/game/maps/";
        }
        else {
            PROJ_PATH = "/maps/";
        }

        FULL_PATH = LOCAL_PATH + PROJ_PATH + "map";

        this.mapID = mapID;
        this.walls = new ArrayList<>();
        this.spawnPositions = new ArrayList<>();
        this.numPhases = 2;

        ArrayList<String> mapStrings = readMapFromSource();

        constructMapFromSource(mapStrings);
    }


    /**
     * Returns an ArrayList of walls that exist in the given phase.
     * @param phase - int representing the phase to test each wall against.
     * @param intactOnly - if true, restricts the resulting ArrayList to only walls that are intact.
     * @return an ArrayList of walls that exist in the given phase.
     */
    public ArrayList<Wall> wallsInPhase(int phase, boolean intactOnly, boolean innerOnly){

        ArrayList<Wall> relWalls = new ArrayList<>();


        for (Wall thisWall : walls) {
            if (thisWall.inPhase(phase)){
                if (!(intactOnly && !thisWall.intact())) {
                    if (!(innerOnly && thisWall.isBoundary())){
                        relWalls.add(thisWall);
                    }

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

    /**
     * Calculates and returns an ArrayList of spaces that movable entities can move and exist within.
     * Assumes boundary walls are included in the map specification file.
     * @return an ArrayList of valid spaces within the map.
     */
    private ArrayList<Vector2> allValidSpace(){

        HashSet<Vector2> validSpaceSet = new HashSet<>();

        // Add all possible map positions.
        for (int x = 0; x < width; x++){
            for (int y = 0; y < length; y++){
                validSpaceSet.add(new Vector2(x, y));
            }
        }

        // Check all (intact) walls and remove each position obstructed by a wall.
        for (Wall w : walls) {
            if (w.isAlive()){
                HashSet<Vector2> wallSpace = w.getWholeWall();
                for (Vector2 space : wallSpace){
                    validSpaceSet.remove(space);
                }
            }
        }

        // Convert the set into an ArrayList and return it.
        ArrayList<Vector2> validSpace = new ArrayList<>();
        for (Vector2 space : validSpaceSet){
            validSpace.add(space);
        }
        return validSpace;
    }


    /**
     * Reads this map's specified source file and returns it as an ArrayList of strings.
     * @return an ArrayList of strings representing the source file.
     */
    public ArrayList<String> readMapFromSource(){

        ArrayList<String> mapStrings = new ArrayList<>();

        try {
            FileReader file = new FileReader(FULL_PATH + mapID + ".txt");
            BufferedReader input = new BufferedReader(file);
            String nextString;
            while ((nextString = input.readLine()) != null) {
                mapStrings.add(nextString);
            }
            input.close();
        } catch (IOException e) {
            System.err.print("ERROR: Invalid Map Source location/file.");
            System.exit(0);
        }

        return mapStrings;
    }

    /**
     * Parses the strings of the source file, constructing the map and checking for
     * invalid walls and spawn zones.
     * @param sourceLines - An ArrayList of the lines of map source text.
     */
    private void constructMapFromSource(ArrayList<String> sourceLines) {

        boolean parsing = true;
        int lineNum = 0;
        String err = "";
        String line;

        // Parse metadata.
        while (parsing && !sourceLines.isEmpty()) {

            err = "(Line " + String.valueOf(lineNum + 1) + ") Map Source Parsing Error: ";
            line = sourceLines.get(0);
            if (line.startsWith("//") || line.equals("")) {         // If the line is blank or a comment, ignore it.
                lineNum++;
                sourceLines.remove(0);
            } else {                                               // Otherwise, parse it.
                List<String> items = Arrays.asList(line.split("\\s*,\\s*"));

                // Parse this map's ID.
                try {
                    mapID = Integer.parseInt(items.get(0));
                } catch (NumberFormatException e) {
                    System.out.println(err + "Map ID not valid. Is it an integer?");
                    System.exit(0);
                }

                // Get this map's name.
                mapName = items.get(1);

                // Parse this map's dimensions.
                try {
                    width = Integer.parseInt(items.get(2));
                    length = Integer.parseInt(items.get(3));
                } catch (NumberFormatException e) {
                    System.out.println(err + "Map Dimensions not valid. Are both values integers?");
                    System.exit(0);
                }

                // Parse this map's maximum number of players.
                try {
                    int tempMapCapacity = Integer.parseInt(items.get(4));
                    if (tempMapCapacity % 4 != 0) {
                        System.out.println(err + "Map capacity is not allowed.");
                        System.exit(0);
                    } else {
                        mapCapacity = tempMapCapacity;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(err + "Map capacity not valid. Is it an integer?");
                    System.exit(0);
                }
                parsing = false;
                sourceLines.remove(0);
                lineNum++;
            }
        }

        // Check we got the data we needed and haven't run out of lines of map source.
        if (sourceLines.isEmpty()) {
            if (mapCapacity == 0) {
                System.out.println(err + "No map metadata provided.");
            } else {
                System.out.println(err + "No walls or spawn locations provided.");
            }
            System.exit(0);
        }

        // Parse walls.
        parsing = true;
        while (parsing && !sourceLines.isEmpty()) {
            err = "(Line " + String.valueOf(lineNum + 1) + ") Map Source Parsing Error: ";
            line = sourceLines.get(0);

            if (line.startsWith("//") || line.equals("")) {          // If the line is blank or a comment, ignore it.
                lineNum++;
                sourceLines.remove(0);
            } else if (line.startsWith("##")) {                     // If the line is a separator, move on.
                lineNum++;
                sourceLines.remove(0);
                parsing = false;
            } else {                                                // Else, parse it.
                try {
                    List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
                    int x1 = Integer.parseInt(items.get(0));
                    int y1 = Integer.parseInt(items.get(1));
                    Vector2 startPos = new Vector2(x1, y1);

                    int x2 = Integer.parseInt(items.get(2));
                    int y2 = Integer.parseInt(items.get(3));
                    Vector2 endPos = new Vector2(x2, y2);

                    if (isBoundary(startPos, endPos)) {
                        for (int phase = 0; phase < 2; phase++){
                            this.walls.add(new Wall(startPos, endPos, phase, false, true));
                        }
                    }
                    else {
                        if (!inMap(startPos) || !inMap(endPos)) {        // Check the wall is within the map.
                            System.out.println(err + "Wall outside of map bounds.");
                            //System.exit(0);
                        }

                        int phase = Integer.parseInt(items.get(4));
                        if (phase > numPhases) {
                            System.out.println(err + "Wall inside invalid phase.");
                            //System.exit(0);
                        }

                        boolean damageable = Boolean.parseBoolean(items.get(5));

                        this.walls.add(new Wall(startPos, endPos, phase, damageable, false));
                    }

                    lineNum++;
                    sourceLines.remove(0);

                } catch (NumberFormatException e) {
                    System.out.println(err + "Invalid wall.");
                    //System.exit(0);
                }
            }
        }

        // Check there are at least 4 walls and haven't run out of lines of map source.
        if (sourceLines.isEmpty()) {
            if (walls.size() < 4) {
                System.out.println(err + "Not enough walls provided.");
            } else {
                System.out.println(err + "No spawn locations provided.");
            }
            System.exit(0);
        }

        // Parse spawn points.
        while (!sourceLines.isEmpty()) {
            err = "(Line " + String.valueOf(lineNum + 1) + ") Map Source Parsing Error: ";
            line = sourceLines.get(0);

            if (line.startsWith("//") || line.equals("")) {          // If the line is blank or a comment, ignore it.
                lineNum++;
                sourceLines.remove(0);
            } else {
                try {
                    List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
                    int x = Integer.parseInt(items.get(0));
                    int y = Integer.parseInt(items.get(1));
                    Vector2 spawnPos = new Vector2(x, y);

                    if (!inMap(spawnPos)) {      // Check the spawn location is within the map.
                        System.out.println(err + "Spawn location outside of map bounds.");
                        //System.exit(0);
                    }

                    int t = Integer.parseInt(items.get(2));
                    if (!(t == 0 || t == 1)){
                        System.out.println(err + "Spawn location belongs to invalid team.");
                        //System.exit(0);
                    }

                    spawnPositions.add(new SpawnPosition(spawnPos, t));
                    lineNum++;
                    sourceLines.remove(0);

                } catch (NumberFormatException e) {
                    System.out.println(err + "Invalid spawn point.");
                    //System.exit(0);
                }
            }
        }

        // Check there are enough spawn points to support the maximum number of players.
        if (spawnPositions.size() < mapCapacity){
            System.out.println(err + "Not enough spawn positions to support the specified map capacity.");
            System.exit(0);
        }
    }

    /**
     * Deduces whether or not a position is within the bounds of this map.
     * @param pos - The position to be tested.
     * @return true if the position is within the bounds of this map.
     */
    public boolean inMap(Vector2 pos) {

        return (pos.getX() >= 0 && pos.getX() <= width && pos.getY() >= 0 && pos.getY() <= length);
    }

    private boolean isBoundary(Vector2 startPos, Vector2 endPos){
        float x1 = startPos.getX();
        float y1 = startPos.getY();
        float x2 = endPos.getX();
        float y2 = endPos.getY();

        return (x1 == 0 || x1 == width) &&
                (y1 == 0 || y1 == length) &&
                (x2 == 0 || x2 == width) &&
                (y2 == 0 || y2 == length);
    }
}
