package com.wolg_vlad.dcrawler.entities.skills.action

import com.wolg_vlad.dcrawler.entities.skills.FloatingDamageMark
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.math.MathAction

open class ActionAdapter(skill: Skill) : Action(skill) {
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

    override fun beforeEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {}
    override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {}
    override fun critSuccessEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {}
    override fun failEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {}
    override fun critFailEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {}
    override fun afterEffect(target: Target, damage: Int, mark: FloatingDamageMark) {}
}