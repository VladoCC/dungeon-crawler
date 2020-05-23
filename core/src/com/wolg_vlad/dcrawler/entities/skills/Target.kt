package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.dungeon.DungeonMap.Companion.getCell
import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 05.07.2017.
 */
class Target(var x: Int, var y: Int) {
    var checkX: Int //todo find a way to work around coordinates for check and for attack
    var checkY: Int
    var isLinked = false
    var main: Target
    val linkedTargets = Array<Target>()

    fun setCheckCoords(checkX: Int, checkY: Int) {
        this.checkX = checkX
        this.checkY = checkY
    }

    fun move(movement: Vector2) {
        x += movement.x.toInt()
        y += movement.y.toInt()
        checkX += movement.x.toInt()
        checkX += movement.y.toInt()
    }

    fun addLinkedTarget(target: Target) {
        linkedTargets.add(target)
        target.isLinked = true
        target.main = this
    }

    /*for (Entity entity : Entity.getPlayingEntities()){
            if (entity.getTileX() == getX() && entity.getTileY() == getY()){
                return entity;
            }
        }
        return null;*/
    val entity: Entity?
        get() {
            val cell = getCell(x, y)
            return cell?.entity
        }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val target = o as Target
        return if (x != target.x) false else y == target.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    init {
        checkX = x
        checkY = y
        main = this
    }
}