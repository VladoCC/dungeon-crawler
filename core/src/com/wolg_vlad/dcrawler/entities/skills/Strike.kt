package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleAttackAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EnemyTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EnemyDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.math.SumAction

/**
 * Created by Voyager on 15.11.2017.
 */
class Strike(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = SumAction(*(Array(2) {DiceAction(6)}))

    override val displayers: kotlin.Array<TargetDisplayer> = arrayOf(EnemyDisplayer())
    override val pattern: TargetPattern = EnemyTargetPattern(this)
    override val icon: Texture = Texture("punch.png")
    override val name: String = "Strike"
    override val description: String = "Strike that deals " + attackAction.description + " damage"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 2
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