package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleAttackAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EnemyTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EnemyDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 02.12.2017.
 */
class Shoot(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(6)

    override var skillAccuracyBonus = 6
    override val displayers: kotlin.Array<TargetDisplayer> = arrayOf(EnemyDisplayer())
    override val pattern: TargetPattern = EnemyTargetPattern(this)
    override val icon: Texture = Texture("shoot.png")
    override val name: String = "Shoot"
    override val description: String = "Shot that deals ${attackAction.description} damage " +
            "and accuracy bonus +$skillAccuracyBonus"
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 16
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
        addPlayContainer().enemyPlay.addAction(SimpleAttackAction(this, attackAction))
    }
}