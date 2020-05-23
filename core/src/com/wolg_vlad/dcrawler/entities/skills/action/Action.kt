package com.wolg_vlad.dcrawler.entities.skills.action

import com.wolg_vlad.dcrawler.entities.skills.FloatingDamageMark
import com.wolg_vlad.dcrawler.entities.skills.Play
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 28.06.2017.
 */
abstract class Action(private val skill: Skill) {
    fun act(target: Target?, success: Int, mark: FloatingDamageMark) {
        val damage = attack(target, success, mark)
        effect(target, success, damage, mark)
    }

    private fun attack(target: Target?, success: Int, mark: FloatingDamageMark): Int {
        var damage = 0
        //count and apply damage only if there is a target
        if (target != null) {
            var action: MathAction? = null
            var critAction: MathAction? = null
            if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                action = successDamage()
            } else if (success == Play.TARGETING_MISS || success == Play.TARGETING_CRIT_MISS) {
                action = failDamage()
            }
            if (action != null) { //add damage from buffs if action is able to do some damage
                if (action.max() > 0) {
                    action = skill.countAttackAction(action)
                } else if (action.max() < 0) {
                    action = skill.countHealAction(action)
                }
                damage += action!!.act()
            }
            if (success == Play.TARGETING_CRIT_HIT) {
                critAction = critSuccessDamage(damage)
            } else if (success == Play.TARGETING_CRIT_MISS) {
                critAction = critFailDamage(damage)
            }
            //apply crit damage if it was crit attack
            if (critAction != null) {
                damage += critAction.act()
            }
            target.entity!!.addHp(-damage)
            if (damage != 0) {
                mark.addText("" + -damage)
            }
        }
        return damage
    }

    private fun effect(target: Target?, success: Int, damage: Int, mark: FloatingDamageMark) {}
    protected abstract fun successDamage(): MathAction?
    protected abstract fun critSuccessDamage(standardDamage: Int): MathAction?
    protected abstract fun failDamage(): MathAction?
    protected abstract fun critFailDamage(standardDamage: Int): MathAction?
    protected abstract fun beforeEffect(target: Target?, damage: Int, mark: FloatingDamageMark?)
    protected abstract fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark)
    protected abstract fun critSuccessEffect(target: Target?, damage: Int, mark: FloatingDamageMark?)
    protected abstract fun failEffect(target: Target?, damage: Int, mark: FloatingDamageMark?)
    protected abstract fun critFailEffect(target: Target?, damage: Int, mark: FloatingDamageMark?)
    protected abstract fun afterEffect(target: Target, damage: Int, mark: FloatingDamageMark)

}