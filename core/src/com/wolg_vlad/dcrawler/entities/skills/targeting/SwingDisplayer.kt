package com.wolg_vlad.dcrawler.entities.skills.targeting

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.skills.Skill

/**
 * Created by Voyager on 13.03.2018.
 */
class SwingDisplayer : TargetDisplayer {
    override fun targetCreation(x: Int, y: Int, array: MutableList<TilePos>, skill: Skill): Boolean {
        array.add(TilePos(x, y, TargetRenderer.defaultTargetTile))
        val range = skill.range
        if (x == skill.doer.tileX && y < skill.doer.tileY) {
            for (i in 0 until range) {
                array.add(TilePos(x - 1 - i, y, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x - 1 - i, y + 1, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x + 1 + i, y, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x + 1 + i, y + 1, TargetRenderer.defaultTargetTile))
            }
            for (i in 1 until range) {
                for (j in -range until range + 1) {
                    array.add(TilePos(x + j, y - i, TargetRenderer.defaultTargetTile))
                }
            }
        } else if (x == skill.doer.tileX && y > skill.doer.tileY) {
            for (i in 0 until range) {
                array.add(TilePos(x - 1 - i, y, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x - 1 - i, y - 1, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x + 1 + i, y, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x + 1 + i, y - 1, TargetRenderer.defaultTargetTile))
            }
            for (i in 1 until range) {
                for (j in -range until range + 1) {
                    array.add(TilePos(x + j, y + i, TargetRenderer.defaultTargetTile))
                }
            }
        } else if (x > skill.doer.tileX && y == skill.doer.tileY) {
            for (i in 0 until range) {
                array.add(TilePos(x, y - 1 - i, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x - 1, y - 1 - i, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x, y + 1 + i, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x - 1, y + 1 + i, TargetRenderer.defaultTargetTile))
            }
            for (i in 1 until range) {
                for (j in -range until range + 1) {
                    array.add(TilePos(x + i, y + j, TargetRenderer.defaultTargetTile))
                }
            }
        } else if (x < skill.doer.tileX && y == skill.doer.tileY) {
            for (i in 0 until range) {
                array.add(TilePos(x, y - 1 - i, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x + 1, y - 1 - i, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x, y + 1 + i, TargetRenderer.defaultTargetTile))
                array.add(TilePos(x + 1, y + 1 + i, TargetRenderer.defaultTargetTile))
            }
            for (i in 1 until range) {
                for (j in -range until range + 1) {
                    array.add(TilePos(x - i, y + j, TargetRenderer.defaultTargetTile))
                }
            }
        }
        return true
    }
}