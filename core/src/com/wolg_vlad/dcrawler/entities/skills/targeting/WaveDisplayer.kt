package com.wolg_vlad.dcrawler.entities.skills.targeting

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.ai.AIUtils
import com.wolg_vlad.dcrawler.entities.skills.Skill

/**
 * Created by Voyager on 12.03.2018.
 */
class WaveDisplayer : TargetDisplayer {
    override fun targetCreation(x: Int, y: Int, array: MutableList<TilePos>, skill: Skill): Boolean {
        val range = skill.range
        val poses = AIUtils.getCellRaytrace(skill.doer.tileX, skill.doer.tileY, x, y, range - 1)
        poses!!.clip(poses.size - range, poses.size - 1)
        for (pos in poses) {
            array.add(TilePos(pos!!.x.toInt(), pos.y.toInt(), TargetRenderer.defaultTargetTile))
        }
        return true
    }
}