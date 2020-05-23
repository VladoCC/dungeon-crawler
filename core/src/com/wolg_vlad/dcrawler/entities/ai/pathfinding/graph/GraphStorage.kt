package com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph

import com.badlogic.gdx.math.Vector2
import com.wolg_vlad.dcrawler.dungeon.DungeonMap

/**
 * Created by Voyager on 30.04.2017.
 */
object GraphStorage {
    private lateinit var nodesBottom: Array<Array<CellNode?>>
    private val nodesTop = com.badlogic.gdx.utils.Array<RoomNode>()

    fun createBottomGraph() {
        val layer = DungeonMap.tileLayer
        val width = layer!!.layerWidth
        val height = layer.layerHeight
        nodesBottom = Array(width) { i ->
            val inner = Array(height) { j ->
                val cell = layer.getCell(i, j)
                if (cell != null) {
                    val node = CellNode(i, j, cell)
                    return@Array node
                }
                return@Array null
            }
            return@Array inner as Array<CellNode?>
        }
        for (i in 0 until width) {
            for (j in 0 until height) {
                val node: Node? = getNodeBottom(i, j)
                if (node != null) {
                    getNodeBottom(i, j - 1)?.let { node.connect(it) }
                    getNodeBottom(i, j + 1)?.let { node.connect(it) }
                    getNodeBottom(i - 1, j)?.let { node.connect(it) }
                    getNodeBottom(i + 1, j)?.let { node.connect(it) }
                }
            }
        }
        restoreNodeStates()
    }

    fun addTopNode(node: RoomNode) {
        nodesTop.add(node)
        for (graphNode in nodesTop) {
            node.connect(graphNode)
            graphNode.connect(node)
        }
    }

    fun moveNodesTop(movement: Vector2) {
        for (node in nodesTop) {
            node.move(movement)
            println(node.x.toString() + " " + node.y)
        }
    }

    fun getNodeTop(x: Int, y: Int): RoomNode? {
        for (node in nodesTop) {
            if (node.x == x && node.y == y) {
                return node
            }
        }
        return null
    }

    fun getNodeBottom(x: Int, y: Int): CellNode? {
        return if (x >= 0 && x < nodesBottom.size && y >= 0 && y < nodesBottom[0].size) {
            nodesBottom[x][y]
        } else null
    }

    fun restoreBottomNodeStates() {
        for (nodes in nodesBottom) {
            for (node in nodes) {
                node?.restoreState()
            }
        }
    }

    fun restoreTopNodeStates() {
        for (node in nodesTop) {
            node.restoreState()
        }
    }

    fun restoreNodeStates() {
        restoreBottomNodeStates()
        restoreTopNodeStates()
    }
}