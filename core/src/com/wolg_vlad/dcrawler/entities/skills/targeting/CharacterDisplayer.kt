package com.wolg_vlad.dcrawler.entities.skills.targeting

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.dungeon.DungeonMap
import com.wolg_vlad.dcrawler.entities.skills.Skill

/**
 * Created by Voyager on 08.03.2018.
 */
class CharacterDisplayer : TargetDisplayer {
    override fun targetCreation(x: Int, y: Int, array: MutableList<TilePos>, skill: Skill): Boolean {
        if (DungeonMap.getCell(x, y) != null && DungeonMap.getCell(x, y)!!.entity != null
                && DungeonMap.getCell(x, y)!!.entity!!.isCharacter) {
            array.add(TilePos(x, y, TargetRenderer.defaultTargetTile))
            return true
        }
        return false
    }
}