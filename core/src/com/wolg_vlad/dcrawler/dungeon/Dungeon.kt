package com.wolg_vlad.dcrawler.dungeon

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.Character
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.GraphStorage
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.RoomNode
import com.wolg_vlad.dcrawler.story.quest.Quest
import com.wolg_vlad.dcrawler.utils.SeededRandom

/**
 * Created by Voyager on 25.03.2018.
 */
class Dungeon @JvmOverloads constructor(val quest: Quest, private val charPoses: Array<Vector2> = defaultCharPoses) {
    var placedRoomsCount = 0
    val roomsPool = Array<Room>()
    val roomsPlaced = Array<Room>()

    val endRoom: Int
        get() = quest.endRoom

    val roomCountMax: Int
        get() = quest.roomsMax

    fun moveRooms(move: Vector2) {
        for (room in roomsPlaced) {
            room.changePos(move)
        }
    }

    fun getPlacedRoom(roomX: Int, roomY: Int): Room? {
        for (room in roomsPlaced) {
            if (room.x == roomX && room.y == roomY) {
                return room
            }
        }
        return null
    }

    fun popRoom(): Room {
        val room = roomsPool[0]
        roomsPool.removeIndex(0)
        roomsPlaced.add(room)
        placedRoomsCount++
        return room
    }

    fun placeRoom(x: Int, y: Int, side: Int): Room {
        val room = popRoom()
        room.rotate(side)
        val roomX = x / ROOM_WIDTH
        val roomY = y / ROOM_HEIGHT
        println("$roomX,ggnbgn $roomY")
        room.setPos(roomX, roomY)
        val sides = Array<Int>()
        val left = getPlacedRoom(roomX - 1, roomY)
        val right = getPlacedRoom(roomX + 1, roomY)
        val up = getPlacedRoom(roomX, roomY - 1)
        val down = getPlacedRoom(roomX, roomY + 1)
        var leftExit = false
        var rightExit = false
        var topExit = false
        var bottomExit = false
        if (left != null) {
            if (left.hasExit(Exit.DIRECTION_EAST)) {
                println("Left: Yes")
                leftExit = true
                room.addDoor(Exit.DIRECTION_WEST)
                val doorPoint = left.getExit(Exit.DIRECTION_EAST)!!.exitCells[0]
                println("Door removed! Cords: (" + doorPoint!!.x + "; " + doorPoint.y + ")")
                Door.removeDoor(doorPoint.x, doorPoint.y)
            }
        } else {
            sides.add(Exit.DIRECTION_WEST)
        }
        if (right != null) {
            if (right.hasExit(Exit.DIRECTION_WEST)) {
                println("Right: Yes")
                rightExit = true
                room.addDoor(Exit.DIRECTION_EAST)
                val doorPoint = right.getExit(Exit.DIRECTION_WEST)!!.exitCells[0]
                println("Door removed! Cords: (" + doorPoint!!.x + "; " + doorPoint.y + ")")
                Door.removeDoor(doorPoint.x, doorPoint.y)
            }
        } else {
            sides.add(Exit.DIRECTION_EAST)
        }
        if (up != null) {
            if (up.hasExit(Exit.DIRECTION_SOUTH)) {
                println("Up: Yes")
                topExit = true
                room.addDoor(Exit.DIRECTION_NORTH)
                val doorPoint = up.getExit(Exit.DIRECTION_SOUTH)!!.exitCells[0]
                println("Door removed! Cords: (" + doorPoint!!.x + "; " + doorPoint.y + ")")
                Door.removeDoor(doorPoint.x, doorPoint.y)
            }
        } else {
            sides.add(Exit.DIRECTION_NORTH)
        }
        if (down != null) {
            if (down.hasExit(Exit.DIRECTION_NORTH)) {
                println("Down: Yes")
                bottomExit = true
                room.addDoor(Exit.DIRECTION_SOUTH)
                val doorPoint = down.getExit(Exit.DIRECTION_NORTH)!!.exitCells[0]
                println("Door removed! Cords: (" + doorPoint!!.x + "; " + doorPoint.y + ")")
                Door.removeDoor(doorPoint.x, doorPoint.y)
            }
        } else {
            sides.add(Exit.DIRECTION_SOUTH)
        }
        room.addExits(sides)
        GraphStorage.addTopNode(RoomNode(roomX, roomY, room))
        room.complete()
        for (exit in room.exits) {
            val cells = exit.exitCells
            for (cell in cells!!) {
                cell!!.x += x
                cell!!.y += y
            }
            Door(exit.direction, cells)
        }
        return room
    }

    private fun createRooms(count: Int) {
        val exitRoom = endRoom
        println("$exitRoom - exit room")
        for (i in 0 until count) {
            var room: Room
            if (i == 0) {
                room = createRoom(true)
            } else {
                room = createRoom(false)
                if (i == exitRoom - 1) {
                    room = quest.exitRoomConstruction(room)
                }
            }
            roomsPool.add(room)
        }
    }

    private fun createRoom(first: Boolean): Room {
        val room: Room
        if (first) {
            room = Room("rooms/default.room")
            room.setMobs(false)
        } else {
            val rnd = SeededRandom.getInstance().nextInt(10)
            room = if (rnd > 3) {
                Room("rooms/corridor.room")
            } else {
                Room("rooms/default.room")
            }
        }
        return room
    }

    fun placeChars() {
        for (i in 0 until Character.chars.size) {
            val character = Character.getChar(i)
            val pos = charPoses[i]
            character.setCellPos(pos)
        }
    }

    companion object {
        const val ROOM_WIDTH = 8
        const val ROOM_HEIGHT = 8
        private val defaultCharPoses: Array<Vector2>
            private get() {
                val array = Array<Vector2>()
                array.add(Vector2(3F, 3F))
                array.add(Vector2(4F, 3F))
                array.add(Vector2(4F, 4F))
                array.add(Vector2(3F, 4F))
                return array
            }
    }

    init {
        createRooms(quest.roomsMax)
        placeChars()
        Exit.resetExitsCount()
        Exit.exitsMax = quest.roomsMax - 1
    }
}