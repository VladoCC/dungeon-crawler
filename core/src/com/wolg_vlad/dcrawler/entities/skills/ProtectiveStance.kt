package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.effects.ProtectionEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleCombinedAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EntityTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EntityDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 05.11.2017.
 */
class ProtectiveStance(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(4)
    var armor = 3

    override val displayers: Array<TargetDisplayer> = arrayOf(EntityDisplayer())
    override val pattern: TargetPattern = EntityTargetPattern(this)
    override val icon: Texture = Texture("protectivestance.png")
    override val name: String = "Protective stance"
    override val description: String = "You deal ${attackAction.description} damage and anyway take up " +
            "a protective stance that gives you 5 additional armor"
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
        addPlayContainer().playerPlay.addAction(object : SimpleCombinedAction(this, attackAction) {
            public override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {}
            override fun afterEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                doer.addEffect(ProtectionEffect(doer, armor))
            }
        })
    }
}