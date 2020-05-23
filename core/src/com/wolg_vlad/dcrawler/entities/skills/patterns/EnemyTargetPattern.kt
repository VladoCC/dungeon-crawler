package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.dungeon.DungeonMap.Companion.getCell
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

class EnemyTargetPattern(skill: Skill) : TargetPattern(skill) {
    override fun createTarget(target: Target): Target {
        return target
    }

    override fun isValid(target: Target): Boolean {
        val cell = getCell(target.checkX, target.checkY)
        return cell?.entity?.isEnemy?:false
    }
}