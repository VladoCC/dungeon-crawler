package com.wolg_vlad.dcrawler.entities.ai.pathfinding

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.dungeon.Dungeon
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.GraphStorage
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.Node

/**
 * Created by Voyager on 02.05.2017.
 */
object Pathfinder {
    const val PATH_COUNT = 1
    fun searchRoomPath(startCellX: Int, startCellY: Int, endCellX: Int, endCellY: Int): NodePath? {
        val x1 = startCellX / Dungeon.ROOM_WIDTH
        val x2 = endCellX / Dungeon.ROOM_WIDTH
        val y1 = startCellY / Dungeon.ROOM_HEIGHT
        val y2 = endCellY / Dungeon.ROOM_HEIGHT
        val startNode: Node? = GraphStorage.getNodeTop(x1, y1)
        val endNode: Node? = GraphStorage.getNodeTop(x2, y2)
        return searchConnectionPath(startNode, endNode, -1, 1, false)
    }

    fun searchConnectionPath(startNode: Node?, endNode: Node?, limit: Int, ignoreLast: Boolean): NodePath? {
        return searchConnectionPath(startNode, endNode, limit, PATH_COUNT, ignoreLast)
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
    @JvmOverloads
    fun searchConnectionPath(startNode: Node?, endNode: Node?, limit: Int, pathCount: Int = PATH_COUNT, ignoreLast: Boolean = false): NodePath? {
        var limit = limit
        println("Pathfinding")
        if (startNode == null || endNode == null) {
            return null
        }
        var endPath: NodePath? = null
        val path = NodePath(startNode)
        if (startNode == endNode) {
            return path
        }
        val paths = Array<NodePath>()
        paths.add(path)
        var found = 0
        if (limit < 0) {
            limit = Int.MAX_VALUE
        }
        while (found < pathCount) {
            val thisPath = paths[0]
            val node = thisPath.last
            for (connection in node.connections) {
                val newNode = connection.toNode
                // check to see if it's our target
                val end = newNode == endNode
                // node counts as available to check if it is reachable
                // or target node (if we ignore last reachability check)
                val available = newNode!!.isReachable || end && ignoreLast
                // it shouldn't be checked before (it should be open)
                // or this path is more effective (less cost) then one that was found earlier
                val costDifference = if (newNode!!.isOpen) 0 else thisPath.cost + connection.cost - newNode.path!!.cost
                val effective = newNode!!.isOpen || costDifference < 0
                // path cost should be within limits to maintain this path
                val withinLimit = path.cost + connection.cost <= limit
                if (available && effective && withinLimit) {
                    val newPath = thisPath!!.clone()
                    newPath!!.add(connection)
                    newNode.path = newPath
                    if (end) {
                        endPath = newPath
                        found++
                    } else {
                        // this node was used in other path and we found that this path is better
                        // so we need to remove other path
                        if (costDifference < 0) {
                            paths.removeValue(newNode.path, true)
                        }
                        paths.add(newPath)
                    }
                }
            }
            paths.removeIndex(0)
            if (paths.size == 0) {
                break
            }
        }
        GraphStorage.restoreNodeStates()
        return endPath
    }
}