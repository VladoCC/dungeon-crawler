package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.entities.ai.AIUtils
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

class FloorWaveTargetPattern(skill: Skill) : ValidatedTargetPattern(skill) {
    override fun createTarget(target: Target): Target {
        val range = skill.range
        val cellX = target.x
        val cellY = target.y
        val charX = skill.doer.tileX
        val charY = skill.doer.tileY
        val array = AIUtils.getCellRaytrace(charX, charY, cellX, cellY, range - 1)
        array.clip(array.size - range + 1, array.size - 1)
        for (pos in array) {
            val linked = Target(pos!!.x.toInt(), pos.y.toInt())
            linked.main = target
            linked.isLinked = true
            target.addLinkedTarget(linked)
        }
        return target
    }
}