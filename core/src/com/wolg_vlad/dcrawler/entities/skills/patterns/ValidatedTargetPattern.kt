package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

abstract class ValidatedTargetPattern(skill: Skill): TargetPattern(skill) {
    override fun isValid(target: Target) = true
}