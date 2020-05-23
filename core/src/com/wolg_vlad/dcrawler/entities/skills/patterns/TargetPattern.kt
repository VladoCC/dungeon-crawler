package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

abstract class TargetPattern(val skill: Skill) {
    abstract fun createTarget(target: Target): Target

    abstract fun isValid(target: Target): Boolean
}