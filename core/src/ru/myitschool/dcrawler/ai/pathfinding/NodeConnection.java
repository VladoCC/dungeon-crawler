package ru.myitschool.dcrawler.ai.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Created by Voyager on 01.05.2017.
 */
public class NodeConnection implements Connection<Node> {

    Node fromNode;
    Node toNode;
    int cost;

    public NodeConnection(Node fromNode, Node toNode, int cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }
}
