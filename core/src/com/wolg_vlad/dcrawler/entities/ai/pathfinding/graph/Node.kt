package com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.NodePath

/**
 * Created by Voyager on 01.05.2017.
 */
abstract class Node(var x: Int, var y: Int) {
    enum class State {
        OPEN, CLOSED
    }

    var path: NodePath? = null
    val connections: Array<NodeConnection>

    fun move(movement: Vector2) {
        x += movement.x.toInt()
        y += movement.y.toInt()
    }

    val isOpen: Boolean
        get() = path == null

    fun restoreState() {
        path = null
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val node = o as Node
        return x == node.x &&
                y == node.y
    }

    /**
     * Creates [NodeConnection] from this node to other one if they [Node.canConnect]
     * @param to end node for connection
     * @return true if connection successfully created and false otherwise
     */
    fun connect(to: Node): Boolean { //TODO might want to change function name
        val can = canConnect(to)
        if (can) {
            connections.add(NodeConnection(this, to, connectionCost(to)))
        }
        return can
    }

    abstract fun canConnect(node: Node): Boolean
    abstract fun connectionCost(to: Node?): Int
    abstract val isReachable: Boolean

    companion object {
        protected fun canConnect(node1: Node, node2: Node): Boolean {
            return node1.canConnect(node2)
        }
    }

    init {
        connections = Array()
    }
}