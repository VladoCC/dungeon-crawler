package com.wolg_vlad.dcrawler.entities.skills.action

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.math.NumberAction

class SimpleAttackAction(skill: Skill, private val action: MathAction) : ActionAdapter(skill) {
    private val critAction: MathAction
    override fun successDamage(): MathAction? {
        return action
    }

    override fun critSuccessDamage(standardDamage: Int): MathAction? {
        return critAction
    }

    init {
        critAction = NumberAction(action.max())
    }
}