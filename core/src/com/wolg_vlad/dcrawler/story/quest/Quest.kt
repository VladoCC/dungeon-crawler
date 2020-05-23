package com.wolg_vlad.dcrawler.story.quest

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.wolg_vlad.dcrawler.dungeon.Room
import com.wolg_vlad.dcrawler.ui.screens.FinalScreen
import com.wolg_vlad.dcrawler.utils.SeededRandom

/**
 * Created by Voyager on 21.03.2018.
 */
abstract class Quest(endRoom: Int, private val deltaPos: Int, roomsMax: Int) {
    val endRoom: Int
    val roomsMax: Int
    private var room: Room? //todo rework. Why should all of the quests have room?

    constructor(roomPos: Int, deltaPos: Int, roomsMax: Int, room: Room) : this(roomPos, deltaPos, roomsMax) {
        this.room = room
    }

    fun getRoom(): Room? {
        return room
    }

    fun setRoom(room: Room) {
        this.room = room
    }

    fun exitRoomConstruction(room: Room): Room {
        var room = room
        if (getRoom() != null) {
            room = getRoom() as Room
        }
        room!!.setMobs(false)
        room = modifyRoom(room)
        return room
    }

    abstract fun modifyRoom(room: Room): Room
    abstract fun exitRoomOpened()
    abstract fun targetReached()
    fun complete() {
        (Gdx.app.applicationListener as Game).screen = FinalScreen("Complete!")
    }

    companion object {
        val exitColor = Color(-0x33dd78)
    }

    init {
        if (roomsMax >= endRoom + deltaPos) {
            this.roomsMax = roomsMax
        } else {
            this.roomsMax = endRoom + deltaPos
        }
        this.endRoom = endRoom + SeededRandom.getInstance().nextInt(deltaPos + 1)
        room = null
    }
}