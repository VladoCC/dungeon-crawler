package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleAttackAction
import com.wolg_vlad.dcrawler.entities.skills.check.PlayCheck
import com.wolg_vlad.dcrawler.entities.skills.patterns.EntityTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.EntityDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.math.NegativeAction

/**
 * Created by Voyager on 26.11.2017.
 */
class HealOrKill(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(6)
    var healAction: MathAction = NegativeAction(attackAction)

    override val displayers: Array<TargetDisplayer> = arrayOf(EntityDisplayer())
    override val pattern: TargetPattern = EntityTargetPattern(this)
    override val icon: Texture = Texture("heal.png")
    override val name: String = "Heal or Kill"
    override val description: String = "You can heal your ally or damage enemy for ${healAction.description}"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 8
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_AT_WILL
    override val targetType: Int = SKILL_TARGET_TYPE_ENTITY
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = false
    override val isWallTargets: Boolean = false

    init {
        val container = addPlayContainer()
        container.playerPlay.setPlayCheck(object : PlayCheck(this) {
            override fun check(target: Target): Int {
                return Play.TARGETING_HIT
            }
        })
        container.enemyPlay.addAction(SimpleAttackAction(this, attackAction))
        container.playerPlay.addAction(SimpleAttackAction(this, healAction))
    }
}