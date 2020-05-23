package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

class FloorSingleTargetPattern(skill: Skill) : ValidatedTargetPattern(skill) {
    override fun createTarget(target: Target): Target {
        return target
    }
}