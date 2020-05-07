package ru.myitschool.dcrawler.entities.ai.pathfinding.graph;

/**
 * Created by Voyager on 01.05.2017.
 */
public class NodeConnection {

    Node fromNode;
    Node toNode;
    int cost;

    public NodeConnection(Node fromNode, Node toNode, int cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }
}
