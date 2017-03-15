package server.ai.pathfinding;
import server.ai.Intel;
import server.game.Vector2;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Created by Ciprian on 03/05/17.
 */
public class AStar {
    Intel intel;
    public Node[][] nodeses;
    public AStar (Intel intel)
    {
        this.intel=intel;
    }

    public  List<Node> printPath(Node target){
        List<Node> path = new ArrayList<Node>();

        for(Node node = target; node!=null; node = node.parent){
            path.add(node);
        }
        Collections.reverse(path);

        return path;
    }

    public Node getNode(int x,int y)
    {
        return nodeses[x][y];
    }

    public void makeGraph(Node goal,int phase)
    {
        server.game.Map map=this.intel.getMap();
        int radius=this.intel.ent().getRadius();
        int width=map.getMapWidth();
        int height=map.getMapLength();
        Node[][] nodes;

        nodes = new Node[width][height];
        for(int i=0;i<height;i++)
        {
            nodes[0][i]=new Node(new Vector2(0,i),this.intel.ent().getRadius(),phase,intel,goal.coordinates());
            nodes[0][i].setH_scores(10000);
        }

        for(int i=0;i<width;i++)
        {
            nodes[i][0]=new Node(new Vector2(i,0),this.intel.ent().getRadius(),phase,intel,goal.coordinates());
        }

        for ( int row = radius; row < (height - radius); row+=radius )
        {
            for ( int col = radius; col < (width - radius); col+=radius )
            {

                //System.out.println("Col: " + col);
                nodes[col][row]=new Node(new Vector2(col,row),this.intel.ent().getRadius(),phase,intel,goal.coordinates());

                ArrayList<Edge> adj = new ArrayList<Edge>();

                adj.add(new Edge(nodes[col-radius][row],nodes[col-radius][row].h_scores));
                adj.add(new Edge(nodes[col-radius][row-radius],nodes[col-radius][row-radius].h_scores));
                adj.add( new Edge(nodes[col][row-radius],nodes[col][row-radius].h_scores));
                adj.add(new Edge(nodes[col-radius][row],nodes[col-radius][row].h_scores));
                //adj.add(new Edge(nodes[col-1][row],nodes[col-1][row].h_scores));
                //System.out.println(nodes[col][row]==null);
                nodes[col][row].addAdjancencies(adj);

                nodes[col-radius][row].addAdjancency(new Edge(nodes[col][row],nodes[col][row].h_scores));
                nodes[col-radius][row-radius].addAdjancency(new Edge(nodes[col][row],nodes[col][row].h_scores));
                nodes[col][row-radius].addAdjancency(new Edge(nodes[col][row],nodes[col][row].h_scores));
                nodes[col-radius][row-radius].addAdjancency(new Edge(nodes[col][row],nodes[col][row].h_scores));
            }

        }

        this.nodeses=nodes;

    }


    public Vector2 getClosestNode(Node target,int radius) {
        Vector2 actualCoordiates = target.coordinates();
        int x = (int) actualCoordiates.getX();
        int y = (int) actualCoordiates.getY();
        int min = 1000;
        int newX = 0,newY=0;
        for (int row = radius; row < (intel.getMap().getMapLength() - radius); row += radius)
            for (int col = radius; col < (intel.getMap().getMapWidth() - radius); col += radius) {
               if(abs (nodeses[col][row].getX()-x)+abs(nodeses[col][row].getY()-y)<min)
               {
                   min=abs (nodeses[col][row].getX()-x)+abs(nodeses[col][row].getY()-y);
                   newX=row;
                   newY=col;
               }



    }
        return new Vector2(newX,newY);
    }




     public void AstarSearch(Node source, Node goal){

        Set<Node> explored = new HashSet<Node>();
        //    System.out.println("Checking if it s receiving what it should-->"+source.adjacencies.toString());
        PriorityQueue<Node> queue = new PriorityQueue<Node>(200,
                new Comparator<Node>(){
                    //override compare method
                    public int compare(Node i, Node j){
                        if(i.f_scores > j.f_scores){
                            return 1;
                        }

                        else if (i.f_scores < j.f_scores){
                            return -1;
                        }

                        else{
                            return 0;
                        }
                    }

                }
        );

        //cost from start
        source.g_scores = 0;

        queue.add(source);
      //  System.out.println(source.adjacencies.size());
        boolean found = false;

        while((!queue.isEmpty())&&(!found)){
                //System.out.println(queue.toString());
            //the node in having the lowest f_score value
            Node current = queue.poll();

            explored.add(current);
           // System.out.println(explored.toString());
            //goal found
            if(current.coordinates().equals(goal.coordinates())){

                found = true;
            }


            //check every child of current node
            for(Edge e : current.adjacencies){
                Node child = e.target;
                double cost = e.cost;
              //  System.out.print(cost+" ");
                double temp_g_scores = current.g_scores + cost;
                double temp_f_scores = temp_g_scores + child.h_scores;


                                /*if child node has been evaluated and
                                the newer f_score is higher, skip*/

                if((explored.contains(child)) &&
                        (temp_f_scores >= child.f_scores)){
                    continue;
                }

                                /*else if child node is not in queue or
                                newer f_score is lower*/

                else if((!queue.contains(child)) ||
                        (temp_f_scores < child.f_scores)){

                    child.parent = current;
                    child.g_scores = temp_g_scores;
                    child.f_scores = temp_f_scores;

                    if(queue.contains(child)){
                        queue.remove(child);
                    }

                    queue.add(child);

                }

            }

        }

    }

}


