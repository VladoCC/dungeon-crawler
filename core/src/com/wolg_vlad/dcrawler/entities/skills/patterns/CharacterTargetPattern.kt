package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.dungeon.DungeonMap
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

class CharacterTargetPattern(skill: Skill) : TargetPattern(skill) {
    override fun createTarget(target: Target): Target {
        return target
    }

    override fun isValid(target: Target): Boolean {
        val cell = DungeonMap.getCell(target.checkX, target.checkY)
        return cell?.entity?.isCharacter?:false
    }
}