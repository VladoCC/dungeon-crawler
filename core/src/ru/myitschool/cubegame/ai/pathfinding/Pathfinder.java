package ru.myitschool.cubegame.ai.pathfinding;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonMap;

/**
 * Created by Voyager on 02.05.2017.
 */
public class Pathfinder {

    public static final int PATH_COUNT = 1;
    public static final int PATH_MAX_LENGTH_CELL = 100;
    public static final int PATH_MAX_LENGTH_ROOM = 50;

    public static NodePath searchRoomPath(int startCellX, int startCellY, int endCellX, int endCellY){
        int x1 = startCellX / DungeonMap.ROOM_WIDTH;
        int x2 = endCellX / DungeonMap.ROOM_WIDTH;
        int y1 = startCellY / DungeonMap.ROOM_HEIGHT;
        int y2 = endCellY / DungeonMap.ROOM_HEIGHT;

        Node startNode = GraphStorage.getNodeTop(x1, y1);
        Node endNode = GraphStorage.getNodeTop(x2, y2);
        return searchConnectionPath(startNode, endNode, true, PATH_MAX_LENGTH_ROOM, 1, true, false);
    }

    public static NodePath searchConnectionPath(Node startNode, Node endNode, boolean limited, int limit){
        return searchConnectionPath(startNode, endNode, limited, limit, PATH_COUNT, false, false);
    }

    public static NodePath searchConnectionPath(Node startNode, Node endNode, boolean limited, int limit, boolean ignoreLast){
        return searchConnectionPath(startNode, endNode, limited, limit, PATH_COUNT, false, ignoreLast);
    }

    public static NodePath searchConnectionPath(Node startNode, Node endNode, boolean limited, int limit, int pathCount, boolean oneNode, boolean ignoreLast) {
        GraphStorage.restoreNodesState();
        if (startNode == null || endNode == null){
            return null;
        }
        NodePath outPath = new NodePath();
        Array<NodePath> paths = new Array<ru.myitschool.cubegame.ai.pathfinding.NodePath>();
        Array<NodePath> endPaths = new Array<ru.myitschool.cubegame.ai.pathfinding.NodePath>();
        NodePath path = new NodePath();
        path.add(startNode);
        if (startNode == endNode) {
            if (oneNode) {
                return path;
            }
            return null;
        }
        paths.add(path);
        int found = 0;
        int lim = PATH_MAX_LENGTH_CELL;
        if (limited){
            lim = limit;
        }
        while (found < pathCount) {
            NodePath thisPath = paths.get(0);
            Node node = thisPath.getLast();
            Array<NodeConnection> connections = node.getConnections();
            for (int i = 0; i < connections.size; i++) {
                NodeConnection connection = connections.get(i);
                Node newNode = connection.getToNode();
                if (newNode.getState() == Node.OPEN_STATE || (newNode == endNode && ignoreLast)) {
                    NodePath newPath = thisPath.clone();
                    newPath.add(newNode);
                    if (newPath.getCost() > lim && found == 0) {
                        return null;
                    } else if (newPath.getCost() > lim) {
                        return choosePath(outPath, endPaths);
                    }
                    if (newNode.x == endNode.x && newNode.y == endNode.y) {
                        endPaths.add(newPath);
                        found++;
                    } else {
                        newNode.setState(Node.CLOSED_STATE);
                        paths.add(newPath);
                    }
                }
            }
            paths.removeIndex(0);
            if (paths.size == 0 && found == 0){
                return null;
            } else if (paths.size == 0){
                return choosePath(outPath, endPaths);
            }
        }
        return choosePath(outPath, endPaths);
    }

    private static NodePath choosePath(NodePath outPath, Array<NodePath> endPaths) {
        /*endPaths.sort();
        endPaths.reverse();
        System.out.println(endPaths);
        outPath = endPaths.get(0);*/
        outPath = endPaths.get(0);
        for (NodePath path : endPaths) {
            if (path.getCost() < outPath.getCost()){
                outPath = path;
            }
        }
        return outPath;
    }
}
