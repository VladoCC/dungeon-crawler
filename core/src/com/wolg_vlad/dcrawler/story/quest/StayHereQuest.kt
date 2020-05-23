package com.wolg_vlad.dcrawler.story.quest

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.dungeon.Room
import com.wolg_vlad.dcrawler.effects.CellEffect
import com.wolg_vlad.dcrawler.effects.FloorEffect
import com.wolg_vlad.dcrawler.entities.Character
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.story.quest.Quest
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile
import java.util.function.Predicate

/**
 * Created by Voyager on 09.04.2018.
 */
class StayHereQuest(roomPos: Int, deltaPos: Int, roomsMax: Int) : Quest(roomPos, deltaPos, roomsMax) {
    override fun modifyRoom(room: Room): Room {
        val targets: MutableList<Target> = room.reachableCells
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
            override val id: String = "main.dcrawler.effect.quest.stay_here"
            override val expireTurns: Int = 0


            override fun onStepTo(entity: Entity) {
                super.onStepTo(entity)
                val chars: Int = Character.chars.size
                var inZone = 0
                for (cell in floorEffect!!.cells) {
                    if (cell.hasEntity() && cell.entity!!.isCharacter) {
                        inZone++
                    }
                }
                if (inZone >= chars) {
                    complete()
                }
            }
        }
        effect = FloorEffect(targets, cellEffect, exitColor, true, false)
        room.addEffect(effect)
        return room
    }

    override fun exitRoomOpened() {}
    override fun targetReached() {}
}