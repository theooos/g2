package server.ai.altPathfinding;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created by Rhys on 3/21/17.
 */
public class AStar {

    private PriorityQueue<Node> frontier;
    private HashSet<Node> explored;
    private Node[][] map;
    private int mapWidth;
    private int mapHeight;
    private int xSource;
    private int ySource;
    private int xGoal;
    private int yGoal;

    public AStar(Node[][] map) {
        this.map = map;
        mapWidth = map.length;
        mapHeight = map[0].length;
    }

    private void prepareForSearch(int xSource, int ySource, int xGoal, int yGoal){
        frontier.clear();
        explored.clear();

        this.xSource = xSource;
        this.ySource = ySource;
        this.xGoal = xGoal;
        this.yGoal = yGoal;

        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                if (map[x][y] != null) {
                    map[x][y].prepareForSearch(xGoal, yGoal);
                }
            }
        }

        frontier.add(map[xSource][ySource]);
    }
    
}
