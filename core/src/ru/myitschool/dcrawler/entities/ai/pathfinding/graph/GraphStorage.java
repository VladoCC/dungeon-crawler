package ru.myitschool.dcrawler.entities.ai.pathfinding.graph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.dungeon.DungeonCell;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.ui.layers.DynamicTileLayer;

/**
 * Created by Voyager on 30.04.2017.
 */
public class GraphStorage {

    private static CellNode[][] nodesBottom;
    private static Array<RoomNode> nodesTop = new Array<>();

    public static void createBottomGraph(){
        DynamicTileLayer layer = DungeonMap.getTileLayer();
        int width = layer.getWidth();
        int height = layer.getHeight();
        nodesBottom = new CellNode[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                DungeonCell cell = layer.getCell(i, j);
                if (cell != null) {
                    CellNode node = new CellNode(i, j, cell);
                    nodesBottom[i][j] = node;
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Node node = getNodeBottom(i, j);
                if (node != null) {
                    node.connect(getNodeBottom(i, j - 1));
                    node.connect(getNodeBottom(i, j + 1));
                    node.connect(getNodeBottom(i - 1, j));
                    node.connect(getNodeBottom(i + 1, j));
                }
            }
        }
        restoreNodeStates();
    }

    public static void addTopNode(RoomNode node){
        nodesTop.add(node);

        for (Node graphNode : nodesTop){
            node.connect(graphNode);
            graphNode.connect(node);
        }
    }

    public static void moveNodesTop(Vector2 movement){
        for (Node node : nodesTop){
            node.move(movement);
            System.out.println(node.getX() + " " + node.getY());
        }
    }

    public static RoomNode getNodeTop(int x, int y){
        for (RoomNode node : nodesTop){
            if (node.getX() == x && node.getY() == y){
                return node;
            }
        }
        return null;
    }

    public static CellNode getNodeBottom(int x, int y) {
        if (x >= 0 && x < nodesBottom.length && y >= 0 && y < nodesBottom[0].length) {
            return nodesBottom[x][y];
        }
        return null;
    }

    public static void restoreBottomNodeStates() {
        for (CellNode[] nodes : nodesBottom) {
            for (CellNode node : nodes) {
                node.restoreState();
            }
        }
    }

    public static void restoreTopNodeStates() {
        for (Node node : nodesTop) {
            node.restoreState();
        }
    }

    public static void restoreNodeStates(){
        restoreBottomNodeStates();
        restoreTopNodeStates();
    }
}
