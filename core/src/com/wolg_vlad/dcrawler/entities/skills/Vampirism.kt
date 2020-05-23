package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
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
 * Created by Voyager on 02.12.2017.
 */
class Vampirism(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(6)
    var damage = 3
    var turns = 3

    override val displayers: kotlin.Array<TargetDisplayer> = arrayOf(EntityDisplayer())
    override val pattern: TargetPattern = EntityTargetPattern(this)
    override val icon: Texture = Texture("vampirism.png")
    override val name: String = "Vampirism"
    override val description: String = "You deal damage to your target and restore as many hp as it loses. " +
            "If target will be lower then quarter of max hp, it will bleed $turns turn(-s) for $damage damage"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 1
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_AT_WILL
    override val targetType: Int = SKILL_TARGET_TYPE_ENTITY
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = true
    override val isWallTargets: Boolean = false

    init {
        addPlayContainer().enemyPlay.addAction(object : SimpleCombinedAction(this, attackAction) {
            public override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                doer.addHp(damage)
                if (target.entity!!.hp <= target.entity!!.hpMax / 4) {
                    target.entity!!.addEffect(BloodiedEffect(target.entity, NumberAction(damage), turns))
                }
                mark.addText(damage.toString() + "")
                val damageMark = FloatingDamageMark(doer.tileX, doer.tileY, (-damage).toString() + "")
                damageMark.show()
            }
        })
        addPlayContainer()
    }
}