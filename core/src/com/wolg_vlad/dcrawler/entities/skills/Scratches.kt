package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.ComplexAttackAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EntityTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EntityDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 26.10.2017.
 */
class Scratches(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(6)
    var critAction: MathAction = DiceAction(8)
    override var skillAccuracyBonus = 1

    override val displayers: kotlin.Array<TargetDisplayer> = arrayOf(EntityDisplayer())
    override val pattern: TargetPattern = EntityTargetPattern(this)
    override val icon: Texture = Texture("scratches.png")
    override val name: String = "Scratches"
    override val description: String = "You scratch body of your enemy with your claws. " +
            "It is not as effective as weapons, but this can be done faster and more accurately. " +
            "It gives you +$skillAccuracyBonus bonus accuracy. " +
            "And deals ${attackAction.description}(${critAction.description}) damage"
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
        addPlayContainer().playerPlay.addAction(ComplexAttackAction(this, attackAction, critAction, null, null))
    }
}