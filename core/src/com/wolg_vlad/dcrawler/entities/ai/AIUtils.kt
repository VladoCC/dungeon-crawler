package com.wolg_vlad.dcrawler.entities.ai

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.dungeon.Dungeon
import com.wolg_vlad.dcrawler.dungeon.DungeonMap
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.NodePath
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.Pathfinder
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.GraphStorage
import com.wolg_vlad.dcrawler.utils.AdvancedArray
import java.util.*

/**
 * Created by Voyager on 17.08.2017.
 */
object AIUtils {
    /** lightwieght function to get minimal possible distance from one point to another */
    fun countDistanceRaw(startX: Int, startY: Int, endX: Int, endY: Int): Int {
        return Math.abs(startX - endX) + Math.abs(startY - endY)
    }

    fun getCellRaytrace(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int, additional: Int): AdvancedArray<Vector2?> {
        var xStart = xStart
        var yStart = yStart
        val cells = AdvancedArray<Vector2?>()
        val dx = Math.abs(xEnd - xStart)
        val dy = Math.abs(yEnd - yStart)
        val sx = if (xStart < xEnd) 1 else -1
        val sy = if (yStart < yEnd) 1 else -1
        var err = dx - dy
        var finish = false
        var count = 0
        while (!finish || count < additional) {
            cells.add(Vector2(xStart.toFloat(), yStart.toFloat()))
            if (finish) {
                count++
            }
            if (xStart == xEnd && yStart == yEnd) {
                finish = true
            }
            val e2 = 2 * err
            if (e2 > -dy) {
                err = err - dy
                xStart = xStart + sx
            }
            if (e2 < dx) {
                err = err + dx
                yStart = yStart + sy
            }
        }
        return cells
    }

    fun isPathObstructed(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): Boolean {
        return isPathObstructed(getCellRaytrace(xStart, yStart, xEnd, yEnd, 0))
    }

    fun isPathObstructed(cells: Array<Vector2?>): Boolean {
        for (cell in cells) {
            if (!DungeonMap.getCell(cell!!.x.toInt(), cell.y.toInt())!!.tile.groundTile) {
                return true
            }
        }
        return false
    }

    /** return all cells, that obstructs path */
    fun getPathObstructor(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): Array<Vector2?> {
        return getPathObstructor(getCellRaytrace(xStart, yStart, xEnd, yEnd, 0))
    }

    /** return all cells, that obstructs path */
    fun getPathObstructor(cells: Array<Vector2?>): Array<Vector2?> {
        val obstructors = AdvancedArray<Vector2?>()
        for (cellVect in cells) {
            val cell = DungeonMap.getCell(cellVect!!.x.toInt(), cellVect.y.toInt())
            if (cell == null || !cell.tile.groundTile) {
                obstructors.add(cellVect)
            }
        }
        return obstructors
    }

    /** return indexes of obstructors, but without occupied cells  */
    fun getObstructorIndexes(cells: Array<Vector2?>?): Array<Int> {
        return getObstructorIndexes(cells, false)
    }

    /** return indexes of obstructors  */
    fun getObstructorIndexes(cells: Array<Vector2?>?, checkOccupied: Boolean): Array<Int> {
        val obstructorsPos = Array<Int>()
        for (i in 0 until cells!!.size) {
            val cellVect = cells[i]
            val cell = DungeonMap.getCell(cellVect!!.x.toInt(), cellVect.y.toInt())
            if (cell == null || !cell.tile.groundTile || checkOccupied && cell.isOccupied) {
                obstructorsPos.add(i)
            }
        }
        return obstructorsPos
    }

    fun getLineLength(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): Int {
        return getCellRaytrace(xStart, yStart, xEnd, yEnd, 0).size
    }

    /**
     * search for paths to each entity, which matches criteria
     * @param thisX x coordinate for search to start at
     * @param thisY y coordinate for search to start at
     * @param checkThis if true, entity found at {@param thisX}, {@param thisY} coordinates
     * will be included in search, it will be excluded otherwise
     * @param predicate criteria for entities
     * @return sorted map of paths and entities
     */
    fun getAllEntityPaths(thisX: Int, thisY: Int, checkThis: Boolean, predicate: (Entity) -> Boolean): Map<NodePath, Entity> { //TODO make this function faster
        val map = mutableMapOf<NodePath, Entity>()
        for (entity in Entity.playingEntities) {
            // checking predicate function
            val valid = predicate.invoke(entity)
            // check if valid first then call to search path, because search is heavier operation
            if (valid) {
                val path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(thisX, thisY), GraphStorage.getNodeBottom(entity.tileX, entity.tileY), -1, true)
                // if node count == 1 then it is this this cell
                if (path != null && (path.nodeCount > 1 || checkThis)) {
                    map[path] = entity
                }
            }
        }
        return map.toSortedMap()
    }

    fun getDoorCell(x1: Int, y1: Int, x2: Int, y2: Int, x3: Int, y3: Int): Vector2 { //TODO work only for doors in 2 cells
        var targetX = -1
        var targetY = -1
        if (x3 != x2) {
            if (x3 > x2) {
                targetX = x3 * Dungeon.ROOM_WIDTH
            } else if (x3 < x2) {
                targetX = x3 * Dungeon.ROOM_WIDTH + Dungeon.ROOM_WIDTH - 1
            }
            targetY = y3 * Dungeon.ROOM_HEIGHT + Dungeon.ROOM_HEIGHT / 2
            if (y1 > y2) {
                targetY++
            }
        } else if (y3 != y2) {
            if (y3 > y2) {
                targetY = y3 * Dungeon.ROOM_HEIGHT
            } else if (y3 < y2) {
                targetY = y3 * Dungeon.ROOM_HEIGHT + Dungeon.ROOM_HEIGHT - 1
            }
            targetX = x3 * Dungeon.ROOM_WIDTH + Dungeon.ROOM_WIDTH / 2
            if (x3 > x2) {
                targetX--
            }
        }
        return Vector2(targetX.toFloat(), targetY.toFloat())
    }
}