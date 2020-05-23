package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.effects.ImmobilizedEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleCombinedAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EnemyTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EnemyDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.utils.Utils

/**
 * Created by Voyager on 02.12.2017.
 */
class ShieldBash(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(6)
    var turns = 1
    var distance = 1

    override val displayers: kotlin.Array<TargetDisplayer> = arrayOf(EnemyDisplayer())
    override val pattern: TargetPattern = EnemyTargetPattern(this)
    override val icon: Texture = Texture("shield_bash.png")
    override val name: String = "Shield Bash"
    override val description: String = "You bashes target with your shield immobilizes it for $turns turn(-s), " +
            "throws it back for $distance cell(-s) and deal ${attackAction.description} damage"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 1
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 3
    override val type: Int = SKILL_TYPE_COOLDOWN_DICE
    override val targetType: Int = SKILL_TARGET_TYPE_ENEMY
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = true
    override val isWallTargets: Boolean = false

    init {
        addPlayContainer().enemyPlay.addAction(object : SimpleCombinedAction(this, attackAction) {
            public override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                val entity = target.entity
                Utils.pushEntity(doer, target.entity, distance)
                entity!!.addEffect(ImmobilizedEffect(entity, turns))
            }
        })
    }
}