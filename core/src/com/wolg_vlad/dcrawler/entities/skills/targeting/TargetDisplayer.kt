package com.wolg_vlad.dcrawler.entities.skills.targeting

import com.wolg_vlad.dcrawler.entities.skills.Skill

/**
 * Created by Voyager on 16.03.2018.
 */
interface TargetDisplayer {
    fun targetCreation(x: Int, y: Int, list: MutableList<TilePos>, skill: Skill): Boolean
}