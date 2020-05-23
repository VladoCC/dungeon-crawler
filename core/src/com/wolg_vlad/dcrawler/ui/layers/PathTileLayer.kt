package com.wolg_vlad.dcrawler.ui.layers

import com.wolg_vlad.dcrawler.dungeon.DungeonCell
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.NodePath
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile

/**
 * Created by Voyager on 03.05.2017.
 */
class PathTileLayer(width: Int, height: Int, tileWidth: Int, tileHeight: Int) : DynamicTileLayer(width, height, tileWidth, tileHeight) {
    fun drawPath(path: NodePath) {
        clearLayer()
        val nodes = path.nodes
        println("Nodes size: " + nodes!!.size)
        for (i in 1 until nodes.size) {
            val node = nodes[i]
            val x = node.x
            val y = node.y
            println("Node: $x; $y")
            val cell = DungeonCell()
            cell.setTile(DungeonTile.pathTile)
            setCell(x, y, cell)
        }
    } /*public void drawTargets(Array<Target> targets){
        clearLayer();
        for (Target target : targets){
            DungeonCell cell = new DungeonCell(DungeonTile.targetTile);
            setCell(target.getX(), target.getY(), cell);
        }
    }*/
}