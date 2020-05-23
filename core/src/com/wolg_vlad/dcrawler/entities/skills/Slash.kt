package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleAttackAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.FloorSwingTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.SwingDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 16.11.2017.
 */
class Slash(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(8)

    override val displayers: kotlin.Array<TargetDisplayer> = arrayOf(SwingDisplayer())
    override val pattern: TargetPattern = FloorSwingTargetPattern(this)
    override val icon: Texture = Texture("slash.png")
    override val name: String = "Slash"
    override val description: String = "You swings with your halberd and deal damage to all enemies in front of you"
    override val skillAccuracyBonus: Int = 2
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 1
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_AT_WILL
    override val targetType: Int = SKILL_TARGET_TYPE_FLOOR_SWING
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = true
    override val isWallTargets: Boolean = false

    init {
        addPlayContainer().entityPlay.addAction(SimpleAttackAction(this, attackAction))
    }
}