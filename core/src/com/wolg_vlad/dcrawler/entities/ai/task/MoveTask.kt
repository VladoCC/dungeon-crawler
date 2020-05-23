package com.wolg_vlad.dcrawler.entities.ai.task

import com.wolg_vlad.dcrawler.dungeon.DungeonMap
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.NodePath

/**
 * Created by Voyager on 12.02.2018.
 */
class MoveTask @JvmOverloads constructor(path: NodePath) : Task() {
    private val path: NodePath
    override fun startTask() {
        entity!!.path = path
        entity!!.setMovement()
        DungeonMap.updateEntityPos(entity!!)
    }

    override fun endMove() {
        super.endMove()
        complete()
    }

    init {
        this.path = path
    }
}