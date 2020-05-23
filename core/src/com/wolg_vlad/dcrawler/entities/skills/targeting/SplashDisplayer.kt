package com.wolg_vlad.dcrawler.entities.skills.targeting

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.skills.Skill

/**
 * Created by Voyager on 08.03.2018.
 */
class SplashDisplayer(val center: Boolean) : TargetDisplayer {
    override fun targetCreation(x: Int, y: Int, array: MutableList<TilePos>, skill: Skill): Boolean {
        val range = skill.range
        for (i in -range + 1 until range) {
            for (j in -range + 1 until range) {
                if (!(i == 0 && j == 0 && !center)) {
                    array.add(TilePos(x + i, y + j, TargetRenderer.defaultTargetTile))
                }
            }
        }
        return true
    }

}