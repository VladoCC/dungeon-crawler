package com.wolg_vlad.dcrawler.utils

import com.badlogic.gdx.math.Vector2
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.AIUtils

/**
 * Created by Voyager on 09.05.2018.
 */
object Utils {
    fun getDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return Math.round(Math.sqrt(getDistanceRaw(x1, y1, x2, y2).toDouble())).toInt()
    }

    /** function to count distance without sqrt */
    fun getDistanceRaw(x1: Int, y1: Int, x2: Int, y2: Int): Long {
        return (Math.pow(Math.abs(x1 - x2).toDouble(), 2.0) + Math.pow(Math.abs(y1 - y2).toDouble(), 2.0)).toLong()
    }

    fun isTargetInDistance(x1: Int, y1: Int, x2: Int, y2: Int, distanceMin: Int, distanceMax: Int): Boolean {
        val distance = getDistanceRaw(x1, y1, x2, y2)
        return distanceMin * distanceMin <= distance && distance <= distanceMax * distanceMax
    }

    /**
     * push entity from doer
     */
    fun pushEntity(doer: Entity?, target: Entity?, distance: Int) {
        val array = AIUtils.getCellRaytrace(doer!!.tileX, doer.tileY, target!!.tileX, target.tileY, distance)
        val start = array!![array.size - distance - 1]
        array.clip(array.size - distance, array.size - 1)
        val poses = AIUtils.getObstructorIndexes(array, true)
        var end = array.last
        if (poses!!.size > 0) {
            end = if (poses[0] != 0) {
                array[poses[0]!! - 1]
            } else {
                start
            }
        }
        target.throwEntity(Vector2(end!!.x - start!!.x, end.y - start.y))
    }

    /**
     * pull entity to doer
     */
    fun pullEntity(doer: Entity, target: Entity, distance: Int) {
        val array = AIUtils.getCellRaytrace(doer.tileX, doer.tileY, target.tileX, target.tileY, 0)
        val start = array!![array.size - 1]
        array.clip(array.size - distance - 1, array.size - 2)
        val poses = AIUtils.getObstructorIndexes(array, true)
        var end = array.first
        if (poses!!.size > 0) {
            end = if (poses[0] != 0) {
                array[poses[poses.size - 1]!! - 1]
            } else {
                start
            }
        }
        target.throwEntity(Vector2(end!!.x - start!!.x, end.y - start.y))
    }
}