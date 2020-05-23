package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.effects.ControlledEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleCombinedAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EnemyTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EnemyDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 25.11.2017.
 */
class MindControl(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(4)

    override val displayers: Array<TargetDisplayer> = arrayOf(EnemyDisplayer())
    override val pattern: TargetPattern = EnemyTargetPattern(this)
    override val icon: Texture = Texture("mind_control.png")
    override val name: String = "Mind control"
    override val description: String = "You can control your target for one turn"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 4
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_ENCOUNTER
    override val targetType: Int = SKILL_TARGET_TYPE_ENEMY
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = false
    override val isWallTargets: Boolean = false

    init {
        addPlayContainer().enemyPlay.addAction(object : SimpleCombinedAction(this, attackAction) {
            public override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                target.entity!!.addEffect(ControlledEffect(target.entity))
                mark.addText("Success")
            }
        })
    }
}