package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.AIUtils
import com.wolg_vlad.dcrawler.entities.skills.action.SimpleCombinedAction
import com.wolg_vlad.dcrawler.entities.skills.patterns.EntityTargetPattern
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TilePos
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.ui.tiles.ColorTile
import com.wolg_vlad.dcrawler.utils.Utils

/**
 * Created by Voyager on 06.12.2017.
 */
class ForceWave(doer: Entity) : Skill(doer) {
    var attackAction: MathAction = DiceAction(6)
    var distance = 2

    override val displayers: Array<TargetDisplayer> = arrayOf(object : TargetDisplayer {
        override fun targetCreation(x: Int, y: Int, list: MutableList<TilePos>, skill: Skill): Boolean {
            val raytrace = AIUtils.getCellRaytrace(doer.tileX, doer.tileY, x, y, 2)
            raytrace!!.clip(raytrace.size - 2, raytrace.size - 1)
            val poses = AIUtils.getObstructorIndexes(raytrace, true)
            if (poses!!.size > 0) {
                raytrace.clip(0, poses[0]!! - 1)
            }
            for (vector in raytrace) {
                list.add(TilePos(vector!!.x.toInt(), vector.y.toInt(), ColorTile(Color.YELLOW, 0.4f, true)))
            }
            return true
        }
    })
    override val pattern: TargetPattern = EntityTargetPattern(this)
    override val icon: Texture = Texture("force_wave.png")
    override val name: String = "Force wave"
    override val description: String = "You repels your target for " + distance + "cells"
    override val skillAccuracyBonus: Int = 0
    override val range: Int = 1
    override val distanceMin: Int = 1
    override val distanceMax: Int = 3
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
        addPlayContainer().entityPlay.addAction(object : SimpleCombinedAction(this, attackAction) {
            public override fun successEffect(target: Target, damage: Int, mark: FloatingDamageMark) {
                Utils.pushEntity(doer, target.entity, distance)
                mark.addText("Success")
            }
        })
    }
}