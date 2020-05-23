package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.effects.MarkedEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleEffectAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EnemyTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EnemyDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer

/**
 * Created by Voyager on 28.11.2017.
 */
class Mark(doer: Entity) : Skill(doer) {
    var effect: MarkedEffect? = null
    var penalty = -5

    override val displayers: Array<TargetDisplayer> = arrayOf(EnemyDisplayer())
    override val pattern: TargetPattern = EnemyTargetPattern(this)
    override val icon: Texture = Texture("mark.png")
    override val name: String = "Mark"
    override val description: String = "You mark target and it gets$penalty accuracy penalty for all attacks, " +
            "except attacks targeted to you, but you can place only one mark at the time"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 8
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_AT_WILL
    override val targetType: Int = SKILL_TARGET_TYPE_ENEMY
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = true
    override val isWallTargets: Boolean = false

    init {
        addPlayContainer().enemyPlay.addAction(object : SimpleEffectAction(this) {
            public override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                if (effect != null) {
                    effect!!.entity!!.removeEffect(effect)
                }
                effect = MarkedEffect(target.entity, doer, penalty)
                target.entity!!.addEffect(effect!!)
                mark.addText("Success")
            }
        })
    }
}