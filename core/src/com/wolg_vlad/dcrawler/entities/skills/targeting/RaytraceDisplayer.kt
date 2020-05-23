package com.wolg_vlad.dcrawler.entities.skills.targeting

import com.wolg_vlad.dcrawler.entities.ai.AIUtils
import com.wolg_vlad.dcrawler.entities.skills.Skill

/**
 * Created by Voyager on 14.03.2018.
 */
class RaytraceDisplayer : TargetDisplayer {
        override fun targetCreation(x: Int, y: Int, array: MutableList<TilePos>, skill: Skill): Boolean {
        if (Math.abs(skill.doer.tileX - x) + Math.abs(skill.doer.tileY - y) > 1) {
            val obstruct = skill.isObstruct
            val cells = AIUtils.getCellRaytrace(skill.doer.tileX, skill.doer.tileY, x, y, 0)
            cells!!.clip(1, cells.size - 2)
            var max = cells.size
            val poses = AIUtils.getObstructorIndexes(cells)
            println("X: $x Y: $y")
            if (obstruct) {
                if (poses!!.size > 0) {
                    max = poses[0]!!
                }
            }
            for (i in 0 until max) {
                val cell = cells[i]
                array.add(TilePos(cell!!.x.toInt(), cell.y.toInt(), TargetRenderer.defaultLineTile))
            }
            if (poses!!.size > 0 && obstruct) {
                val cell = cells[poses[0]!!]
                array.add(TilePos(cell!!.x.toInt(), cell.y.toInt(), TargetRenderer.defaultObstructionTile))
                return false
            }
        }
        return true
    }
}