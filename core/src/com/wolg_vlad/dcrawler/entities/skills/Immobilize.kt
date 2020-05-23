package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.effects.DisarmedEffect
import com.wolg_vlad.dcrawler.effects.ImmobilizedEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.ComplexEffectAction
import com.wolg_vlad.dcrawler.entities.skills.check.EntityPlayCheck
import com.wolg_vlad.dcrawler.entities.skills.patterns.EnemyTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EnemyDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer

/**
 * Created by Voyager on 02.12.2017.
 */
class Immobilize(doer: Entity) : Skill(doer) {
    var normalTurns = 1
    var criticalTurns = 2

    override val displayers: Array<TargetDisplayer> = arrayOf(EnemyDisplayer())
    override val pattern: TargetPattern = EnemyTargetPattern(this)
    override val icon: Texture = Texture("immobilization.png")
    override val name: String = "Immobilize"
    override val description: String = "You immobilizes your target for $normalTurns($criticalTurns)"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 8
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 2
    override val type: Int = SKILL_TYPE_COOLDOWN_DICE
    override val targetType: Int = SKILL_TARGET_TYPE_ENEMY
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = false
    override val isWallTargets: Boolean = false

    init {
        val container = addPlayContainer()
        container.enemyPlay.setPlayCheck(EntityPlayCheck(this))
        container.enemyPlay.addAction(object : ComplexEffectAction(this) {
            var turns = 0
            override fun beforeEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {}
            public override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                turns = normalTurns
            }

            override fun critSuccessEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {
                turns = criticalTurns
            }

            override fun failEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {}
            override fun critFailEffect(target: Target?, damage: Int, mark: FloatingDamageMark?) {}
            override fun afterEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                if (turns > 0) {
                    target.entity!!.addEffect(ImmobilizedEffect(target.entity, turns))
                    target.entity!!.addEffect(DisarmedEffect(target.entity, turns))
                }
                if (turns == normalTurns) {
                    mark.addText("Success")
                } else if (turns == criticalTurns) {
                    mark.addText("Critical success")
                }
            }
        })
    }
}