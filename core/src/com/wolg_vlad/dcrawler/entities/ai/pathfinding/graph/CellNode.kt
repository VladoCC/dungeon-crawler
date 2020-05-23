package com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph

import com.wolg_vlad.dcrawler.dungeon.DungeonCell

class CellNode(x: Int, y: Int, val cell: DungeonCell) : Node(x, y) {

    override fun canConnect(node: Node): Boolean {
        if (node !is CellNode) {
            return false
        }
        val distance = Math.abs(node.x - x) + Math.abs(node.y - y)
        // this and other one should be ground tiles and they should be neighbours
        return cell.tile.groundTile && node.cell.tile.groundTile && distance == 1
    }

    override fun connectionCost(to: Node?): Int {
        return (to as CellNode?)!!.cell.tile.hardness
    }

    override val isReachable: Boolean
        get() = !cell.isOccupied

}