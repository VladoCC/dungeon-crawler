package ru.myitschool.dcrawler.ai.pathfinding.graph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.pathfinding.NodePath;

/**
 * Created by Voyager on 01.05.2017.
 */
public abstract class Node {

    public enum State{OPEN, CLOSED}

    private int x;
    private int y;
    private NodePath path = null;
    private boolean occupied = false;

    private Array<NodeConnection> connections;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.connections = new Array<NodeConnection>();
    }

    public Array<NodeConnection> getConnections() {
        return connections;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(Vector2 movement){
        x += movement.x;
        y += movement.y;
    }

    public boolean isOpen() {
        return path == null;
    }

    public NodePath getPath() {
        return path;
    }

    public void setPath(NodePath path) {
        this.path = path;
    }

    public void restoreState() {
        path = null;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x &&
                y == node.y;
    }

    /**
     * Creates {@link NodeConnection} from this node to other one if they {@link Node#canConnect(Node)}
     * @param to end node for connection
     * @return true if connection successfully created and false otherwise
     */
    public boolean connect(Node to) { //TODO might want to change function name
        boolean can = canConnect(to);
        if (can) {
            connections.add(new NodeConnection(this, to, connectionCost(to)));
        }
        return can;
    }

    public abstract boolean canConnect(Node node);

    protected static boolean canConnect(Node node1, Node node2) {
        return node1.canConnect(node2);
    }

    public abstract int connectionCost(Node to);

    public abstract boolean isReachable();
}
