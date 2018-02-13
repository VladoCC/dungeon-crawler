package ru.myitschool.cubegame.ai.pathfinding;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Voyager on 01.05.2017.
 */
public class TileGraph {

    Array<Node> nodes;

    public TileGraph(Array<Node> nodes) {
        this.nodes = nodes;
    }

    public TileGraph() {
        this.nodes = new Array<Node>();
    }

    public int getIndex(Node node) {
        return node.getIndex();
    }

    public int getNodeCount() {
        return nodes.size;
    }

    public void addNode(Node node){
        nodes.add(node);
    }
}
