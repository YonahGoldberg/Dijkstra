import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PathFinding implements ActionListener{

    private PathFindingVisualizer visualizer;
    private Node[][] graph;
    private Node startNode;
    private Node endNode;
    private Timer timer;

    public PathFinding(PathFindingVisualizer visualizer) {
        this.visualizer = visualizer;
        visualizer.setDijkstra(this);
        timer = new Timer(5, this);

        graph = new Node[40][70];
        for(int row = 0; row < graph.length; row++) {
            for (int col = 0; col < graph[0].length; col++) {
                graph[row][col] = new Node(row, col);
            }
        }
        startNode = graph[0][0];
        endNode = graph[0][1];
        startNode.setDistToStart(0);
        setAdjacenciesAndDistances(graph);
    }
    public void setAdjacenciesAndDistances(Node[][] graph) {
        //Corners

        //Top left
        graph[0][0].setAdj(new Node[]{graph[1][0], graph[0][1]});
        graph[0][0].setDist(new double[]{1, 1});
        //Top right
        graph[0][graph[0].length - 1].setAdj(new Node[]{graph[0][graph[0].length - 2], graph[1][graph[0].length - 1]});
        graph[0][graph[0].length - 1].setDist(new double[]{1, 1});
        //Bottom left
        graph[graph.length - 1][0].setAdj(new Node[]{graph[graph.length - 1][1], graph[graph.length - 2][0]});
        graph[graph.length - 1][0].setDist(new double[]{1, 1});
        //Bottom Right
        graph[graph.length - 1][graph[0].length - 1].setAdj(new Node[]{graph[graph.length - 1][graph[0].length - 2], graph[graph.length - 2][graph[0].length - 1]});
        graph[graph.length - 1][graph[0].length - 1].setDist(new double[]{1, 1});

        //Sides

        //Top
        for (int col = 1; col < graph[0].length - 1; col++) {
            graph[0][col].setAdj(new Node[]{graph[0][col - 1], graph[0][col + 1], graph[1][col]});
            graph[0][col].setDist(new double[]{1, 1, 1});
        }
        //Right
        for (int row = 1; row < graph.length - 1; row++) {
            graph[row][graph[0].length - 1].setAdj(new Node[]{graph[row - 1][graph[0].length - 1], graph[row + 1][graph[0].length - 1], graph[row][graph[0].length - 2]});
            graph[row][graph[0].length - 1].setDist(new double[]{1, 1, 1});
        }
        //Bottom
        for (int col = 1; col < graph[0].length - 1; col++) {
            graph[graph.length - 1][col].setAdj(new Node[]{graph[graph.length - 1][col - 1], graph[graph.length - 1][col + 1], graph[graph.length - 2][col]});
            graph[graph.length - 1][col].setDist(new double[]{1, 1, 1});
        }
        //Left
        for (int row = 1; row < graph.length - 1; row++) {
            graph[row][0].setAdj(new Node[]{graph[row - 1][0], graph[row + 1][0], graph[row][1]});
            graph[row][0].setDist(new double[]{1, 1, 1});
        }

        //Middle
        for (int row = 1; row < graph.length - 1; row++) {
            for (int col = 1; col < graph[0].length - 1; col++) {
                graph[row][col].setAdj(new Node[]{graph[row][col - 1], graph[row][col + 1], graph[row - 1][col], graph[row + 1][col]});
                graph[row][col].setDist(new double[]{1, 1, 1, 1});
            }
        }
    }
    public void dijkstra() {
        timer.start();
        //If all nodes are visited return
        int numVisited = 0;
        for (Node[] row : graph) {
            for (Node n : row) {
                if (n.isVisited())
                    numVisited++;
            }
        }
        if (numVisited == graph.length * graph[0].length) {
            timer.stop();
            visualizer.removeSetVisitedBackground();
            visualizer.displayShortestPath(getShortestPath());
            return;
        }

        //Find node with lowest distance from startNode
        Node n = getLowestDistUnvisitedNode();
        visualizer.setVisitedBackground(n.getRow(), n.getCol());
        n.setVisited(true);

        if(endNode.equals(n)) {
            timer.stop();
            visualizer.removeSetVisitedBackground();
            visualizer.displayShortestPath(getShortestPath());
            return;
        }


        //Update adjacent squares to that node's lowest distances to start node
        updateAdjacentLowestDistancesToStart(n);

        //Gets called recursively by timer
    }


    public Node getLowestDistUnvisitedNode() {
        Node lowestDistUnvisitedNode = null;

        for(Node[] row : graph) {
            for (Node n : row) {
                if(!n.isWall()) {
                    if (lowestDistUnvisitedNode == null && !n.isVisited())
                        lowestDistUnvisitedNode = n;
                    else if (lowestDistUnvisitedNode != null && !n.isVisited() && n.getDistToStart() < lowestDistUnvisitedNode.getDistToStart())
                        lowestDistUnvisitedNode = n;
                }
            }
        }

        return lowestDistUnvisitedNode;
    }

    public void updateAdjacentLowestDistancesToStart(Node n) {
        Node[] adj = n.getAdj();
        double[] distances = n.getDist();

        for (int i = 0; i < adj.length; i++) {
            if (n.getDistToStart() + distances[i] < adj[i].getDistToStart()) {
                adj[i].setDistToStart(n.getDistToStart() + distances[i]);
                adj[i].setFromNode(n);

            }
        }
    }

    public ArrayList<Node> getShortestPath() {
        ArrayList<Node> shortestPath = new ArrayList<>();
        Node fromNode = graph[endNode.getRow()][endNode.getCol()];
        while (fromNode.getDistToStart() > 0) {
            if(fromNode.getFromNode() == null)
                return null;

            shortestPath.add(0, fromNode);
            fromNode = fromNode.getFromNode();
        }
        shortestPath.add(0, fromNode);

        return shortestPath;
    }

    public void setStartNode(int row, int col) {
        startNode.setDistToStart(Integer.MAX_VALUE);
        startNode = graph[row][col];
        startNode.setDistToStart(0);
    }

    public void setEndNode(int row, int col) {
        endNode = graph[row][col];
    }

    public Node[][] getGraph() {
        return graph;
    }

    public void reset() {
        timer.stop();
        for(int row = 0; row < graph.length; row++) {
            for (int col = 0; col < graph[0].length; col++) {
                graph[row][col].setDistToStart(Integer.MAX_VALUE);
                graph[row][col].setFromNode(null);
                graph[row][col].setVisited(false);
                graph[row][col].setIsWall(false);
            }
        }
        startNode.setDistToStart(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dijkstra();
    }
}
