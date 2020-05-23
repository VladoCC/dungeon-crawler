package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

class FloorSplashWithoutCenterTargetPattern(skill: Skill) : ValidatedTargetPattern(skill) {
    override fun createTarget(target: Target): Target {
        val range = skill.range
        val cellX = target.x
        val cellY = target.y
        val main = Target(cellX - range + 1, cellY - range + 1)
        main.setCheckCoords(cellX, cellY)
        for (i in 0 until 2 * (range - 1) + 1) {
            for (j in 0 until 2 * (range - 1) + 1) {
                if (!(cellX - range + 1 + i == cellX && cellY - range + 1 + j == cellY) && !(i == 0 && j == 0)) {
                    val linked = Target(cellX - range + 1 + i, cellY - range + 1 + j)
                    linked.main = main
                    linked.isLinked = true
                    main.addLinkedTarget(linked)
                }
            }
        }
        return main
    }
}