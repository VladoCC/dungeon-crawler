package com.wolg_vlad.dcrawler.story.quest

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.dungeon.Room
import com.wolg_vlad.dcrawler.effects.CellEffect
import com.wolg_vlad.dcrawler.effects.FloorEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile

/**
 * Created by Voyager on 08.04.2018.
 */
class ReachRoomQuest(roomPos: Int, deltaPos: Int, roomsMax: Int) : Quest(roomPos, deltaPos, roomsMax) {
    override fun modifyRoom(room: Room): Room {
        val targets = mutableListOf<Target>()
        for (i in 0 until room!!.width) {
            for (j in 0 until room.height) {
                if (room.getCell(i, j) != null && DungeonTile.getTile(room.getCell(i, j)!!).groundTile) {
                    targets.add(Target(i, j))
                }
            }
        }
        var effect: FloorEffect? = null
        val cellEffect: CellEffect = object : CellEffect(effect) {
            override val icon: Texture? = null
            override val name: String = ""
            override val description: String = ""
            override val skillUse: Boolean = false
            override val positive: Boolean = false
            override val stackable: Boolean = false
            override val stackSize: Int = 0
            override val expiring: Boolean = false
            override val id: String = "main.dcrawler.effect.quest.reach_room"
            override val expireTurns: Int = 0

            override fun onStepTo(entity: Entity) {
                super.onStepTo(entity)
                targetReached()
            }
        }
        effect = FloorEffect(targets, cellEffect, Quest.exitColor, true, false)
        room.addEffect(effect)
        return room
    }

    override fun exitRoomOpened() {}
    override fun targetReached() {
        complete()
    }
}