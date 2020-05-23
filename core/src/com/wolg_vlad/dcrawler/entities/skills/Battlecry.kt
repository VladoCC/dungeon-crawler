package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.effects.BattlecryEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.patterns.CharacterTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 18.11.2017.
 */
class Battlecry(doer: Entity) : Skill(doer) {
    var accuracyBonus = 5
    var action: MathAction = DiceAction(6)

    override val displayers: Array<TargetDisplayer> = arrayOf()
    override val pattern: TargetPattern = CharacterTargetPattern(this)
    override val icon: Texture = Texture("battle_cry.png")
    override val name: String = "Battle cry"
    override val description: String = "You gets additional +$accuracyBonus accuracy and additional " +
            "${action.description} damage for all attack on your next turn"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 0
    override val distanceMin: Int = 0
    override val distanceMax: Int = 0
    override val targetCountMax: Int = 0
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_AT_WILL
    override val targetType: Int = SKILL_TARGET_TYPE_SELF
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = false
    override val isObstruct: Boolean = false
    override val isWallTargets: Boolean = false

    override fun use() {
        doer.addEffect(BattlecryEffect(doer, accuracyBonus, action, 1))
    }
}