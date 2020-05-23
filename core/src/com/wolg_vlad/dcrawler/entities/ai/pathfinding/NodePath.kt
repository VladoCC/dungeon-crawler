package com.wolg_vlad.dcrawler.entities.ai.pathfinding

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.Node
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.NodeConnection
import com.wolg_vlad.dcrawler.utils.AdvancedArray

/**
 * Created by Voyager on 01.05.2017.
 */
class NodePath : Comparable<NodePath> {
    var nodes: AdvancedArray<Node>
    private set
    private var connections: AdvancedArray<NodeConnection>
    var cost = 0
        private set

    private constructor(startNode: Node, connections: Array<NodeConnection>, cost: Int) {
        val newNodes = AdvancedArray<Node>()
        newNodes.add(startNode)
        for (i in 0 until connections.size) {
            newNodes.add(connections[i].toNode)
        }
        val newConnections = AdvancedArray<NodeConnection>()
        for (i in 0 until connections.size) {
            newConnections.add(connections[i])
        }
        nodes = newNodes
        this.connections = newConnections
        this.cost = cost
    }

    constructor(startNode: Node) {
        nodes = AdvancedArray()
        nodes.add(startNode)
        connections = AdvancedArray()
    }

    val nodeCount: Int
        get() = nodes.size

    operator fun get(index: Int): Node {
        return nodes[index]
    }

    val last: Node
        get() = nodes[nodes.size - 1]

    fun add(connection: NodeConnection): Boolean {
        val lastNode = nodes.last
        if (connection.fromNode == lastNode) {
            nodes.add(connection.toNode)
            connections.add(connection)
            cost += connection.cost
            return true
        }
        return false
    }

    fun clear() {
        nodes.clear()
    }

    fun reverse() {
        nodes.reverse()
    }

    operator fun iterator(): Iterator<Node>? {
        return null
    }

    fun clone(): NodePath {
        return NodePath(nodes[0], connections, cost)
    }

    override fun compareTo(other: NodePath): Int {
        return cost - other.cost
    }

    override fun toString(): String {
        return cost.toString() + ""
    }

    fun remove(index: Int) {
        nodes.removeIndex(index)
    }

    fun cut(limit: Int): NodePath {
        val nodes = AdvancedArray<Node>()
        var cost = 0
        for (i in 0 until limit) {
            nodes.add(this.nodes[i])
            if (i > 0) {
                cost += this.nodes[i - 1]!!.connectionCost(nodes[i])
            }
        }
        this.cost = cost
        this.nodes = nodes
        return this
    }

    fun cutLast(): NodePath {
        return cut(nodes.size - 1)
    }
}