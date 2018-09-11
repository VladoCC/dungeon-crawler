package ru.myitschool.dcrawler.ai.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.tiles.DungeonTile;

/**
 * Created by Voyager on 01.05.2017.
 */
public class Node {

    public static final int OPEN_STATE = 0;
    public static final int CLOSED_STATE = 1;

    int x;
    int y;
    private int index;
    private int state = OPEN_STATE;
    private boolean occupied = false;

    DungeonTile tile;
    Array<NodeConnection> connections;

    public Node(DungeonTile tile, int x, int y) {
        this.tile = tile;
        this.x = x;
        this.y = y;
        this.index = Indexer.getIndex();
        this.connections = new Array<NodeConnection>();
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.connections = new Array<NodeConnection>();
    }

    public void addConnection(Node endNode, int cost){
        connections.add(new NodeConnection(this, endNode, cost));
    }

    public int getIndex() {
        return index;
    }

    public Array<NodeConnection> getConnections() {
        return connections;
    }

    public DungeonTile getTile() {
        return tile;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}

class Indexer {

    private static int index = 0;

    public static int getIndex(){
        return index++;
    }
}
