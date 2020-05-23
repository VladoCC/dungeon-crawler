package com.wolg_vlad.dcrawler.entities.skills.action

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.math.MathAction

abstract class ComplexEffectAction(skill: Skill) : Action(skill) {
    override fun successDamage(): MathAction? {
        return null
    }

    override fun critSuccessDamage(standardDamage: Int): MathAction? {
        return null
    }

    override fun failDamage(): MathAction? {
        return null
    }

    override fun critFailDamage(standardDamage: Int): MathAction? {
        return null
    }
}