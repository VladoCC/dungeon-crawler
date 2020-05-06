package ru.myitschool.dcrawler.ai.pathfinding.graph;

import com.badlogic.gdx.ai.pfa.Connection;

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
