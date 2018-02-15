package ru.myitschool.cubegame.ai.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonCell;
import ru.myitschool.cubegame.layer.DynamicTileLayer;
import ru.myitschool.cubegame.tiles.DungeonTile;

/**
 * Created by Voyager on 30.04.2017.
 */
public class GraphStorage {

    private static Node[][] nodeArr;
    private static Array<Node> nodesBottom;
    private static Array<Node> nodesTop;

    public static void createBottomGraph(DynamicTileLayer layer){
        int width = layer.getWidth();
        int height = layer.getHeight();
        nodeArr = new Node[width][height];
        nodesBottom = new Array<Node>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                DungeonCell cell = layer.getCell(i, j);
                if (cell != null) {
                    Node node = new Node((DungeonTile) cell.getTile(), i, j);
                    node.setOccupied(cell.isOccupied());
                    nodeArr[i][j] = node;
                    nodesBottom.add(node);
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                DungeonCell cell = layer.getCell(i, j);
                DungeonCell cellUp = layer.getCell(i, j - 1);
                DungeonCell cellDown = layer.getCell(i, j + 1);
                DungeonCell cellLeft = layer.getCell(i - 1, j);
                DungeonCell cellRight = layer.getCell(i + 1, j);

                Node node = nodeArr[i][j];
                if (cell != null && ((DungeonTile) cell.getTile()).isReachable()) {
                    if (cellUp != null && ((DungeonTile) cellUp.getTile()).isReachable()) {
                        Node nodeEnd = nodeArr[i][j - 1];
                        node.addConnection(nodeEnd, ((DungeonTile) cellUp.getTile()).getHardness());
                    }
                    if (cellDown != null && ((DungeonTile) cellDown.getTile()).isReachable()) {
                        Node nodeEnd = nodeArr[i][j + 1];
                        node.addConnection(nodeEnd, ((DungeonTile) cellDown.getTile()).getHardness());
                    }
                    if (cellLeft != null && ((DungeonTile) cellLeft.getTile()).isReachable()) {
                        Node nodeEnd = nodeArr[i - 1][j];
                        node.addConnection(nodeEnd, ((DungeonTile) cellLeft.getTile()).getHardness());
                    }
                    if (cellRight != null && ((DungeonTile) cellRight.getTile()).isReachable()) {
                        Node nodeEnd = nodeArr[i + 1][j];
                        node.addConnection(nodeEnd, ((DungeonTile) cellRight.getTile()).getHardness());
                    }
                }
            }
        }
        restoreNodesState();
    }

    public static void createTopGraph(Node start){
        nodesTop = new Array<Node>();
    }

    public static void addTopNode(Node node, boolean left, boolean right, boolean top, boolean bottom){//top nodes don't have methods for occupied
        System.out.println("!!"+ node.getX() + " " + node.getY());
        Node leftNode = null;
        Node rightNode = null;
        Node topNode = null;
        Node bottomNode = null;

        for (Node graphNode : nodesTop){
            if (node.x - 1 == graphNode.x && node.y == graphNode.y) {
                leftNode = graphNode;
            }
            if (node.x + 1 == graphNode.x && node.y == graphNode.y) {
                rightNode = graphNode;
            }
            if (node.x == graphNode.x && node.y - 1 == graphNode.y) {
                topNode = graphNode;
            }
            if (node.x == graphNode.x && node.y + 1 == graphNode.y) {
                bottomNode = graphNode;
            }
        }

        for (Node node1 : nodesTop){
            System.out.println(node1.getX() + " " + node1.getY());
        }

        nodesTop.add(node);
        if (left){
            node.addConnection(leftNode, 1);
            leftNode.addConnection(node, 1);
        }
        if (right){
            node.addConnection(rightNode, 1);
            rightNode.addConnection(node, 1);
        }
        if (top){
            node.addConnection(topNode, 1);
            topNode.addConnection(node, 1);
        }
        if (bottom){
            node.addConnection(bottomNode, 1);
            bottomNode.addConnection(node, 1);
        }
    }

    public static void moveNodesTop(Vector2 movement){
        for (Node node : nodesTop){
            node.move(movement);
            System.out.println(node.getX() + " " + node.getY());
        }
        System.out.println("ghmgfmfdmgndnsbhg,");
    }

    public static Node getNodeTop(int x, int y){
        for (Node node : nodesTop){
            if (node.getX() == x && node.getY() == y){
                return node;
            }
        }
        return null;
    }

    public static Node getNodeBottom(int x, int y) {
        if (x >= 0 && x < nodeArr.length && y >= 0 && y < nodeArr[0].length) {
            return nodeArr[x][y];
        }
        return null;
    }

    public static void restoreNodesState(){
        for (Node node : nodesBottom) {
            if (node.isOccupied()){
                node.setState(Node.CLOSED_STATE);
            } else {
                node.setState(Node.OPEN_STATE);
            }
        }
        for (Node node : nodesTop) {
            if (node.isOccupied()){
                node.setState(Node.CLOSED_STATE);
            } else {
                node.setState(Node.OPEN_STATE);
            }
        }
    }
}
