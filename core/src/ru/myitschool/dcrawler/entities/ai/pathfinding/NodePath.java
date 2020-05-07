package ru.myitschool.dcrawler.entities.ai.pathfinding;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.ai.pathfinding.graph.Node;
import ru.myitschool.dcrawler.entities.ai.pathfinding.graph.NodeConnection;
import ru.myitschool.dcrawler.utils.AdvancedArray;

import java.util.Iterator;

/**
 * Created by Voyager on 01.05.2017.
 */
public class NodePath implements Comparable<NodePath> {

    private AdvancedArray<Node> nodes;
    private AdvancedArray<NodeConnection> connections;
    private int cost;

    private NodePath(Node startNode, Array<NodeConnection> connections, int cost) {
        AdvancedArray<Node> newNodes = new AdvancedArray<>();
        newNodes.add(startNode);
        for (int i = 0; i < connections.size; i++) {
            newNodes.add(connections.get(i).getToNode());
        }
        AdvancedArray<NodeConnection> newConnections = new AdvancedArray<>();
        for (int i = 0; i < connections.size; i++) {
            newConnections.add(connections.get(i));
        }
        this.nodes = newNodes;
        this.connections = newConnections;
        this.cost = cost;
    }

    public NodePath(Node startNode) {
        nodes = new AdvancedArray<>();
        nodes.add(startNode);
        connections = new AdvancedArray<>();
    }

    public int getNodeCount() {
        return nodes.size;
    }

    public Node get(int index) {
        return nodes.get(index);
    }

    public Node getLast(){
        return nodes.get(nodes.size - 1);
    }

    public boolean add(NodeConnection connection) {
        Node lastNode = nodes.getLast();
        if (connection.getFromNode().equals(lastNode)) {
            nodes.add(connection.getToNode());
            connections.add(connection);
            cost += connection.getCost();
            return true;
        }
        return false;
    }

    public void clear() {
        nodes.clear();
    }

    public void reverse() {
        nodes.reverse();
    }

    public Iterator<Node> iterator() {
        return null;
    }

    public Array<Node> getNodes() {
        return nodes;
    }

    public NodePath clone() {
        return new NodePath(nodes.get(0), connections, cost);
    }

    @Override
    public int compareTo(NodePath o) {
        return o.cost - this.cost;
    }

    @Override
    public String toString() {
        return cost + "";
    }

    public void remove(int index){
        nodes.removeIndex(index);
    }

    public int getCost() {
        return cost;
    }

    public NodePath cut(int limit){
        AdvancedArray<Node> nodes = new AdvancedArray<>();
        int cost = 0;
        for (int i = 0; i < limit; i++) {
            nodes.add(this.nodes.get(i));
            if (i > 0) {
                cost += this.nodes.get(i - 1).connectionCost(nodes.get(i));
            }
        }
        this.cost= cost;
        this.nodes = nodes;
        return this;
    }

    public NodePath cutLast(){
        return cut(nodes.size - 1);
    }
}
