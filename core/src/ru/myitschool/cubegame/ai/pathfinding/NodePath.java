package ru.myitschool.cubegame.ai.pathfinding;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by Voyager on 01.05.2017.
 */
public class NodePath implements GraphPath<Node>, Comparable<NodePath> {

    private Array<Node> nodes = new Array<Node>();
    private int cost;

    private NodePath(Array<Node> nodes, int cost) {
        Array<Node> newNodes = new Array<Node>();
        for (int i = 0; i < nodes.size; i++) {
            newNodes.add(nodes.get(i));
        }
        this.nodes = newNodes;
        this.cost = cost;
    }

    public NodePath() {
    }

    @Override
    public int getCount() {
        return nodes.size;
    }

    @Override
    public Node get(int index) {
        return nodes.get(index);
    }

    public Node getLast(){
        return nodes.get(nodes.size - 1);
    }

    @Override
    public void add(Node node) {
        if (nodes.size > 0) {
            if (node.getTile() != null) {
                cost += node.getTile().getHardness();
            } else {
                cost += 1;
            }
        }
        nodes.add(node);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }

    @Override
    public Iterator<Node> iterator() {
        return null;
    }

    public Array<Node> getNodes() {
        return nodes;
    }

    public NodePath clone() {
        return new NodePath(nodes, cost);
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
        Array<Node> nodes = new Array<Node>();
        int cost = 0;
        for (int i = 0; i < limit; i++) {
            nodes.add(this.nodes.get(i));
            if (i > 0) {
                cost += this.nodes.get(i).getTile().getHardness();
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
