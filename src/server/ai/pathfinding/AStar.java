package server.ai.pathfinding;
import server.ai.Intel;
import server.game.Vector2;

import java.util.*;
/**
 * Created by Ciprian on 03/05/17.
 */
public class AStar {
    Intel intel;
    Node[][] nodes;
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

    private Node getNode(int x,int y,Node[] [] nodes)
    {
        int width=this.intel.getMap().getMapWidth();
        int height=this.intel.getMap().getMapLength();

        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                if(x==j&&y==i)
                    return nodes[i][j];
        return null;
    }

    public void makeGraph(Node goal,int phase)
    {
        server.game.Map map=this.intel.getMap();
        int width=map.getMapWidth();
        int height=map.getMapLength();
        Node[][] nodes;

        nodes = new Node[width][height];

        for ( int row = 0; row < (height - 1); row++ )
        {
            for ( int col = 0; col < (width - 1); col++ )
            {
              //  System.out.println("Row: " + row);
                //System.out.println("Col: " + col);
                nodes[col][row]=new Node(new Vector2(col,row),this.intel.ent().getRadius(),phase,intel,goal.coordinates());

            }

        }

        makeAdjances(nodes,goal.coordinates());
        this.nodes=nodes;

    }

     public void makeAdjances(Node[][] nodes,Vector2 goal)
     {
         server.game.Map map=this.intel.getMap();
         int width=map.getMapWidth();
         int height=map.getMapLength();
         System.out.println("Size 2:"+height*width);
         System.out.println(nodes==null);
        for (int row=1;row<height-2;row++)
        {

            for(int col=1;col<width-2;col++)
            {
               // System.out.println("Check:"+nodes[col][row]==null);



            nodes[col][row].setAdjacencies(
                    new  Edge[]{
                            new Edge(nodes[col-1][row],nodes[col-1][row].h_scores),
                            new Edge(nodes[col-1][row-1],nodes[col-1][row-1].h_scores),
                            new Edge(nodes[col-1][row+1],nodes[col-1][row+1].h_scores),
                            new Edge(nodes[col][row-1],nodes[col][row-1].h_scores),
                            new Edge(nodes[col][row+1],nodes[col][row+1].h_scores),
                            new Edge(nodes[col-1][row],nodes[col-1][row].h_scores),
                            new Edge(nodes[col-1][row-1],nodes[col-1][row-1].h_scores),
                            new Edge(nodes[col-1][row+1],nodes[col-1][row+1].h_scores)
            });



            }
        }
     }


     public void AstarSearch(Node source, Node goal){

        Set<Node> explored = new HashSet<Node>();

        PriorityQueue<Node> queue = new PriorityQueue<Node>(20,
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

        boolean found = false;
         System.out.println(queue.poll().adjacencies==null);
        while((!queue.isEmpty())&&(!found)){

            //the node in having the lowest f_score value
            Node current = queue.poll();

            explored.add(current);

            //goal found
            if(current.xValue==goal.xValue && current.yValue==goal.yValue){
                found = true;
            }


            //check every child of current node
            for(Edge e : current.adjacencies){
                Node child = e.target;
                double cost = e.cost;
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
                        (temp_f_scores > child.f_scores)){

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


