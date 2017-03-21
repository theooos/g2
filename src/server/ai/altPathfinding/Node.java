package server.ai.altPathfinding;

/**
 * Created by rhys on 3/21/17.
 */
public class Node implements Comparable<Node>{

    private int xCo;
    private int yCo;
    private Node prev;
    private int hCost;
    private int gCost;
    private int fCost;

    public Node(int x, int y){
        this.xCo = x;
        this.yCo = y;
        this.hCost = 0;
        this.gCost = 1000000;
        this.fCost = 1000000;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof  Node)){
            return false;
        }
        else {
            return ((Node)o).getX() == xCo && ((Node)o).getY() == yCo;
        }
    }

    public int getX() {
        return xCo;
    }

    public int getY() {
        return yCo;
    }

    public int getHCost() {
        return hCost;
    }

    public void prepareForSearch(int xGoal, int yGoal) {
        int deltaX = Math.abs(xGoal - this.xCo);
        int deltaY = Math.abs(yGoal - this.yCo);
        this.hCost = deltaX + deltaY;
        this.gCost = 1000000;
        this.fCost = 1000000;
    }

    public int getGCost() {
        return gCost;
    }

    public void updateGCost(int gCost, Node prev) {
        if (gCost < this.gCost) {
            this.gCost = gCost;
            this.fCost = this.gCost + this.hCost;
            this.prev = prev;
        }
    }

    public int getFCost() {
        return fCost;
    }

    @Override
    public int compareTo(Node n2) {
        if (this.fCost > n2.getFCost()) {
            return 1;
        }
        else if (this.fCost < n2.getFCost()) {
            return -1;
        }
        else {
            if (this.hCost > n2.getHCost()) {
                return 1;
            }
            else if (this.hCost < n2.getHCost()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}
