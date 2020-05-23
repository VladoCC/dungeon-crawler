package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

class FloorSplashTargetPattern(skill: Skill) : ValidatedTargetPattern(skill) {
    override fun createTarget(target: Target): Target {
        val range = skill.range
        val cellX = target.x
        val cellY = target.y
        for (i in 0 until 2 * (range - 1) + 1) {
            for (j in 0 until 2 * (range - 1) + 1) {
                if (!(cellX - range + 1 + i == cellX && cellY - range + 1 + j == cellY)) {
                    val linked = Target(cellX - range + 1 + i, cellY - range + 1 + j)
                    linked.main = target
                    linked.isLinked = true
                    target.addLinkedTarget(linked)
                }
            }
        }
        return target
    }
}