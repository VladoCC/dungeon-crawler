package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleAttackAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.FloorSplashTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.SplashDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 25.11.2017.
 */
class Bomb(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(4)

    override val displayers: Array<TargetDisplayer> = arrayOf(SplashDisplayer(true))
    override val pattern: TargetPattern = FloorSplashTargetPattern(this)
    override val icon: Texture = Texture("bomb.png")
    override val name: String = "Bomb"
    override val description: String = "Deal ${attackAction.description} damage to all creatures in small zone"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 2
    override val distanceMin: Int = 2
    override val distanceMax: Int = 8
    override val targetCountMax: Int = 1
    override val cooldownMax: Int = 0
    override val type: Int = SKILL_TYPE_AT_WILL
    override val targetType: Int = SKILL_TARGET_TYPE_FLOOR_SPLASH
    override val isCheckAllTargets: Boolean = false
    override val isMarkEverything: Boolean = false
    override val isMark: Boolean = true
    override val isObstruct: Boolean = true
    override val isWallTargets: Boolean = true

    init {
        addPlayContainer().entityPlay.addAction(SimpleAttackAction(this, attackAction))
    }
}