package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.effects.BloodiedEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleCombinedAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EntityTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EntityDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.math.NumberAction

/**
 * Created by Voyager on 18.11.2017.
 */
class JaggedSword(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(6)
    var normalDamage = 3
    var criticalDamage = 5
    var rounds = 3

    override val displayers: Array<TargetDisplayer> = arrayOf(EntityDisplayer())
    override val pattern: TargetPattern = EntityTargetPattern(this)
    override val icon: Texture = Texture("blood.png")
    override val name: String = "Jagged sword"
    override val description: String = "Deal some damage and adds bleed to target"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 1
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 4
    override val type: Int = SKILL_TYPE_COOLDOWN
    override val targetType: Int = SKILL_TARGET_TYPE_ENTITY
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = true
    override val isWallTargets: Boolean = false

    init {
        addPlayContainer().enemyPlay.addAction(object : SimpleCombinedAction(this, attackAction) {
            var bleedDamage = 0
            override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                bleedDamage = normalDamage
            }

            override fun critSuccessEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {
                bleedDamage = criticalDamage
            }

            override fun afterEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                if (bleedDamage > 0) {
                    val entity = target.entity
                    entity!!.addEffect(BloodiedEffect(entity, NumberAction(bleedDamage), rounds))
                }
                if (bleedDamage == normalDamage) {
                    mark.addText("Success")
                } else if (bleedDamage == criticalDamage) {
                    mark.addText("Critical success")
                }
            }
        })
    }
}