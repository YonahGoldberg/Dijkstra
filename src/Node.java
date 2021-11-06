public class Node {
    private final int row;
    private final int col;
    //Adjacent node, distance
    private Node[] adj;
    //Distances from this node to the node at the corresponding position in adj
    private double[] dist;
    private double distToStart;
    //The previous node in the shortest dist back from this node to the start
    private Node fromNode;
    private boolean isVisited;
    private boolean isWall;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        dist = null;
        adj = null;
        fromNode = null;
        isVisited = false;
        distToStart = Integer.MAX_VALUE;
    }

    public void setIsWall(boolean isWall) {
        this.isWall = isWall;
        if(isWall)
            isVisited = true;
        else
            isVisited = false;
    }

    public boolean isWall() {
        return isWall;
    }


    public Node[] getAdj() {
        return adj;
    }

    public double[] getDist() {
        return dist;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public double getDistToStart() {
        return distToStart;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public void setDistToStart(double distToStart) {
        this.distToStart = distToStart;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public void setAdj(Node[] adj) {
        this.adj = adj;
    }

    public void setDist(double[] dist) {
        this.dist = dist;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
