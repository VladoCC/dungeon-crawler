package com.wolg_vlad.dcrawler.entities.ai.task

import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

/**
 * Created by Voyager on 12.02.2018.
 */
class SkillTask : Task {
    private var skill: Skill
    private var targets = mutableListOf<Target>()

    constructor(entity: Entity?, skill: Skill) {
        this.skill = skill
    }

    constructor(skill: Skill, target: Target) : this(skill, mutableListOf()) {
        addTarget(target)
    }

    constructor(skill: Skill, targets: List<Target>) {
        this.skill = skill
        this.targets = targets.toMutableList()
    }

    fun addTarget(target: Target) {
        targets!!.add(target)
    }

    override fun startTask() {
        entity!!.setUsedSkill(skill)
        entity!!.addTargets(targets)
    }

    override fun endSkill() {
        complete()
    }
}