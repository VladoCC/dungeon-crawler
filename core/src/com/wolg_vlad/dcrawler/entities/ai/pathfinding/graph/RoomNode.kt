package com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph

import com.wolg_vlad.dcrawler.dungeon.Exit
import com.wolg_vlad.dcrawler.dungeon.Room

class RoomNode(x: Int, y: Int, private val room: Room) : Node(x, y) {
    override fun canConnect(node: Node): Boolean {
        var exit = false
        if (x - 1 == node.x && y == node.y) {
            exit = room.hasExit(Exit.DIRECTION_WEST)
        }
        if (x + 1 == node.x && y == node.y) {
            exit = room.hasExit(Exit.DIRECTION_EAST)
        }
        if (x == node.x && y - 1 == node.y) {
            exit = room.hasExit(Exit.DIRECTION_NORTH)
        }
        if (x == node.x && y + 1 == node.y) {
            exit = room.hasExit(Exit.DIRECTION_SOUTH)
        }
        return exit
    }

    override fun connectionCost(to: Node?): Int {
        return 1
    }

    override val isReachable: Boolean
        get() = true

}