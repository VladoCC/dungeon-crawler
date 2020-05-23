package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.effects.ArcaneShieldingEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleEffectAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.CharacterTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.CharacterDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetRenderer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 26.11.2017.
 */
class ArcaneShielding(doer: Entity) : Skill(doer) {
    override val displayers: Array<TargetDisplayer> = arrayOf(CharacterDisplayer())
    override val pattern: TargetPattern = CharacterTargetPattern(this)
    override val icon: Texture = Texture("gods_protection.png")
    override val name: String = "Arcane shielding"
    override val description: String = "This shield will protect its target from any damage for one turn"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 0
    override val distanceMax: Int = 4
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_ENCOUNTER
    override val targetType: Int = SKILL_TARGET_TYPE_CHARACTER
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = false
    override val isWallTargets: Boolean = false

    init {
        addPlayContainer().playerPlay.addAction(object : SimpleEffectAction(this) {
            public override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                target.entity!!.addEffect(ArcaneShieldingEffect(target.entity))
            }
        })
    }
}