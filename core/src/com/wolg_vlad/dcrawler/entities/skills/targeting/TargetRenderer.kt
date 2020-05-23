package com.wolg_vlad.dcrawler.entities.skills.targeting

import com.badlogic.gdx.graphics.Color
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.ui.tiles.ColorTile
import com.wolg_vlad.dcrawler.utils.Utils

/**
 * Created by Voyager on 28.02.2018.
 */
class TargetRenderer(val skill: Skill) {
    val displayers = mutableListOf<TargetDisplayer>()
    fun displayTarget(x: Int, y: Int): List<TilePos> { //TODO wall checks
        val array = mutableListOf<TilePos>()
        val doer = skill.doer
        val distanceMin = skill.distanceMin
        val distanceMax = skill.distanceMax
        if (Utils.isTargetInDistance(x, y, doer!!.tileX, doer.tileY, distanceMin, distanceMax) && !skill.hasTarget(Target(x, y))) {
            targetCreation(x, y, array)
        }
        return array
    }

    protected fun targetCreation(x: Int, y: Int, list: MutableList<TilePos>): List<TilePos> {
        var activate = true
        for (displayer in displayers) {
            if (activate) {
                activate = displayer.targetCreation(x, y, list, skill)
            }
        }
        return list
    }

    fun addDisplayer(displayer: TargetDisplayer) {
        displayers.add(displayer)
    }

    fun addDisplayers(displayers: Array<TargetDisplayer>) {
        displayers.forEach {
            this.displayers.add(it)
        }
    }

    companion object {
        val defaultTargetTile = ColorTile(Color.RED, 0.6f, true)
        val defaultLineTile = ColorTile(Color.CYAN, 0.6f, true)
        val defaultObstructionTile = ColorTile(Color.FIREBRICK, 1F, true)

    }

}