package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleAttackAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.FloorWaveTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.WaveDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 02.12.2017.
 */
class SpearSting(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(10)

    override val displayers: kotlin.Array<TargetDisplayer> = arrayOf(WaveDisplayer())
    override val pattern: TargetPattern = FloorWaveTargetPattern(this)
    override val icon: Texture = Texture("spear_sting.png")
    override val name: String = "Spear sting"
    override val description: String = "You attacks all enemies that stands in two cells in front of you " +
            "and deal ${attackAction.description} damage"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 2
    override val distanceMin: Int = 1
    override val distanceMax: Int = 1
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_AT_WILL
    override val targetType: Int = SKILL_TARGET_TYPE_FLOOR_WAVE
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = true
    override val isWallTargets: Boolean = false

    init {
        addPlayContainer().entityPlay.addAction(SimpleAttackAction(this, attackAction))
    }
}