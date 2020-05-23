package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.effects.CloudOfKnivesEffect
import com.wolg_vlad.dcrawler.effects.FloorClearingEffect
import com.wolg_vlad.dcrawler.effects.FloorEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleAttackAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.FloorSplashTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.SplashDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 30.11.2017.
 */
class CloudOfKnives(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(6)

    override val displayers: Array<TargetDisplayer> = arrayOf(SplashDisplayer(true))
    override val pattern: TargetPattern = FloorSplashTargetPattern(this)
    override val icon: Texture = Texture("cloud_knives.png")
    override val name: String = "Cloud of Knives"
    override val description: String = "You creates cloud of psi energy, that deal damage to all creatures in zone instantly " +
                "and every time creature starts turn in zone on enter the zone to the end of your next turn"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 3
    override val distanceMin: Int = 3
    override val distanceMax: Int = 4
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_ENCOUNTER
    override val targetType: Int = SKILL_TARGET_TYPE_FLOOR_SPLASH
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = false
    override val isWallTargets: Boolean = true

    override fun use() {
        var effect: FloorEffect? = null
        effect = FloorEffect(targets, CloudOfKnivesEffect(effect, countAttackAction(attackAction)), color, true, true)
        doer.addEffect(FloorClearingEffect(effect, 1))
        super.use()
    }

    companion object {
        private val color = Color(-0x5fdf0f41)
    }

    init {
        addPlayContainer().entityPlay.addAction(SimpleAttackAction(this, attackAction))
    }
}