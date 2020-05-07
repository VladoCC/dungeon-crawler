package ru.myitschool.dcrawler.entities.ai.pathfinding;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.ai.pathfinding.graph.GraphStorage;
import ru.myitschool.dcrawler.entities.ai.pathfinding.graph.Node;
import ru.myitschool.dcrawler.entities.ai.pathfinding.graph.NodeConnection;
import ru.myitschool.dcrawler.dungeon.Dungeon;

/**
 * Created by Voyager on 02.05.2017.
 */
public class Pathfinder {

    public static final int PATH_COUNT = 1;

    public static NodePath searchRoomPath(int startCellX, int startCellY, int endCellX, int endCellY){
        int x1 = startCellX / Dungeon.ROOM_WIDTH;
        int x2 = endCellX / Dungeon.ROOM_WIDTH;
        int y1 = startCellY / Dungeon.ROOM_HEIGHT;
        int y2 = endCellY / Dungeon.ROOM_HEIGHT;

        Node startNode = GraphStorage.getNodeTop(x1, y1);
        Node endNode = GraphStorage.getNodeTop(x2, y2);
        return searchConnectionPath(startNode, endNode, -1, 1, false);
    }

    public static NodePath searchConnectionPath(Node startNode, Node endNode, int limit){
        return searchConnectionPath(startNode, endNode, limit, PATH_COUNT, false);
    }

    public static NodePath searchConnectionPath(Node startNode, Node endNode, int limit, boolean ignoreLast){
        return searchConnectionPath(startNode, endNode, limit, PATH_COUNT, ignoreLast);
    }

    /**
     * AStar algorithm to find path from one node to another
     * @param startNode starting point for search
     * @param endNode end target for search
     * @param limit for cost of resulting path
     * @param pathCount limit for possible upgrades of path before return
     * @param ignoreLast if true, reachability of the endNode will be ignored
     * @return path from start node to end node
     */
    public static NodePath searchConnectionPath(Node startNode, Node endNode, int limit, int pathCount, boolean ignoreLast) {
        if (startNode == null || endNode == null){
            return null;
        }
        NodePath endPath = null;

        NodePath path = new NodePath(startNode);
        if (startNode.equals(endNode)) {
            return path;
        }
        Array<NodePath> paths = new Array<>();
        paths.add(path);

        int found = 0;
        if (limit < 0){
            limit = Integer.MAX_VALUE;
        }

        while (found < pathCount) {
            NodePath thisPath = paths.get(0);
            Node node = thisPath.getLast();
            for (NodeConnection connection : node.getConnections()) {
                Node newNode = connection.getToNode();

                // check to see if it's our target
                boolean end = newNode.equals(endNode);
                // node counts as available to check if it is reachable
                // or target node (if we ignore last reachability check)
                boolean available = newNode.isReachable() || (end && ignoreLast);
                // it shouldn't be checked before (it should be open)
                // or this path is more effective then one that was found earlier
                int costDifference = (thisPath.getCost() + connection.getCost()) - newNode.getPath().getCost();
                boolean effective = newNode.isOpen() || costDifference > 0;
                // path cost should be within limits to maintain this path
                boolean withinLimit = path.getCost() + connection.getCost() <= limit;

                if (available && effective && withinLimit) {
                    NodePath newPath = thisPath.clone();
                    newPath.add(connection);
                    newNode.setPath(newPath);

                    if (end) {
                        endPath = newPath;
                        found++;
                    } else {
                        // this node was used in other path and we found that this path is better
                        // so we need to remove other path
                        if (costDifference > 0) {
                            paths.removeValue(newNode.getPath(), true);
                        }
                        paths.add(newPath);
                    }
                }
            }
            paths.removeIndex(0);
            if (paths.size == 0){
                break;
            }
        }
        GraphStorage.restoreNodeStates();
        return endPath;
    }
}
