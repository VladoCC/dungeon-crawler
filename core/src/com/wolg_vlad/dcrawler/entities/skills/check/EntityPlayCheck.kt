package com.wolg_vlad.dcrawler.entities.skills.check

import com.wolg_vlad.dcrawler.entities.skills.Play
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.math.DiceAction

/**
 * Created by Voyager on 19.04.2018.
 */
class EntityPlayCheck(skill: Skill) : PlayCheck(skill) {
    override fun check(target: Target): Int {
        val roll = DiceAction(20).act()
        if (roll == 20) {
            return Play.TARGETING_CRIT_HIT
        } else if (roll == 1) {
            return Play.TARGETING_CRIT_MISS
        }
        return if (roll + skill.getAccuracyBonus(target.entity!!) > target.entity!!.armor) Play.TARGETING_HIT else Play.TARGETING_MISS
    }
}