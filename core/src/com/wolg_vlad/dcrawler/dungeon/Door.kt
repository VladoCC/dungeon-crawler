package com.wolg_vlad.dcrawler.dungeon

import com.badlogic.gdx.math.Vector2
import java.awt.Point

/**
 * Created by Voyager on 09.05.2017.
 */
class Door(val direction: Int, var doorCells: Array<Point?>?) {

    fun moveDoor(tiles: Vector2) {
        for (cell in doorCells!!) {
            cell!!.x += tiles.x.toInt()
            cell!!.y += tiles.y.toInt()
        }
    }

    companion object {
        val doors = com.badlogic.gdx.utils.Array<Door>()
        @JvmStatic
        fun getDoorIndex(x: Int, y: Int): Int {
            for (i in 0 until doors.size) {
                val door = doors[i]
                for (cell in door.doorCells!!) {
                    if (cell!!.x == x && cell.y == y) {
                        return i
                    }
                }
            }
            return -1
        }

        fun getDoor(x: Int, y: Int): Door? {
            return getDoor(getDoorIndex(x, y))
        }

        @JvmStatic
        fun getDoor(index: Int): Door? {
            return if (index != -1) {
                doors[index]
            } else null
        }

        fun removeDoor(x: Int, y: Int) {
            removeDoor(getDoorIndex(x, y))
        }

        @JvmStatic
        fun removeDoor(index: Int) {
            if (index != -1) {
                doors.removeIndex(index)
            }
        }

        @JvmStatic
        fun moveDoors(tiles: Vector2) {
            for (door1 in doors) {
                for (cell in door1.doorCells!!) {
                    println("Door cell: (" + cell!!.x + "; " + cell.y + ")")
                }
            }
            println()
            for (door in doors) {
                door.moveDoor(tiles)
            }
            for (door1 in doors) {
                for (cell in door1.doorCells!!) {
                    println("Door cell: (" + cell!!.x + "; " + cell.y + ")")
                }
            }
        }

    }

    init {
        doors.add(this)
    }
}