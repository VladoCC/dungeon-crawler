package com.wolg_vlad.dcrawler.entities.skills.action

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.math.MathAction

class ComplexAttackAction(skill: Skill, private val successAction: MathAction, private val critSuccessAction: MathAction, private val failAction: MathAction?, private val critFailAction: MathAction?) : ActionAdapter(skill) {
    override fun successDamage(): MathAction? {
        return successAction
    }

    override fun critSuccessDamage(standardDamage: Int): MathAction? {
        return critSuccessAction
    }

    override fun failDamage(): MathAction? {
        return failAction
    }

    override fun critFailDamage(standardDamage: Int): MathAction? {
        return critFailAction
    }

}