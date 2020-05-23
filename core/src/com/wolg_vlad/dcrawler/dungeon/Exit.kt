package com.wolg_vlad.dcrawler.dungeon

import java.awt.Point

/**
 * Created by Voyager on 23.04.2017.
 */
class Exit(private val cellCount: Int, var direction: Int) {
    val exitCells: Array<Point?>
    private var filledCells = 0
    var isOpened = false
    val isFilled: Boolean
        get() = filledCells == cellCount

    fun addCell(cell: Point?): Boolean {
        if (filledCells < cellCount) {
            exitCells[filledCells] = cell
            filledCells++
            return true
        }
        return false
    }

    fun rotate(rotation: Int, roomWidth: Int, roomHeight: Int) {
        for (i in 0 until rotation) { //TODO remake rotations
            direction++
            if (direction == 4) {
                direction = 0
            }
            for (point in exitCells) {
                val x = point!!.getX().toInt()
                val y = point.getY().toInt()
                point.x = roomWidth - y - 1
                point.y = x
            }
        }
    }

    companion object {
        const val DIRECTION_NORTH = 0
        const val DIRECTION_EAST = 1
        const val DIRECTION_SOUTH = 2
        const val DIRECTION_WEST = 3
        @JvmField
        val EXIT_SIDES = arrayOf("North", "East", "South", "West")
        var exitsMax = 0
        var exitsCount = 0
            private set

        fun canOpenDoor(): Boolean {
            return exitsCount < exitsMax
        }

        val exitsLeft: Int
            get() = exitsMax - exitsCount

        fun resetExitsCount() {
            exitsCount = 0
        }

    }

    init {
        if (!isOpened) {
            exitsCount++
        }
        exitCells = arrayOfNulls(cellCount)
    }
}