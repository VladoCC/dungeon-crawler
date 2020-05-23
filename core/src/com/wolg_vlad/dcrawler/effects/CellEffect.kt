package com.wolg_vlad.dcrawler.effects

import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 01.12.2017.
 */
abstract class CellEffect(var floorEffect: FloorEffect?) : Effect(null) {

    fun stepToAction(x1: Int, y1: Int, x2: Int, y2: Int, entity: Entity) {
        if (!hasCell(x1, y1) && hasCell(x2, y2)) {
            onStepTo(entity)
            println("TO ZONE!!!")
        } else if (hasCell(x1, y1) && hasCell(x2, y2)) {
            onMovingInZone(entity)
            println("IN ZONE!!!")
        }
    }

    protected open fun onStepTo(entity: Entity) {}
    protected fun onMovingInZone(entity: Entity) {}
    fun stepFromAction(x1: Int, y1: Int, x2: Int, y2: Int, entity: Entity) {
        if (!hasCell(x2, y2) && hasCell(x1, y1)) {
            onStepFrom(entity)
            println("FROM ZONE!!!")
        }
    }

    protected open fun onStepFrom(entity: Entity?) {}
    private fun hasCell(x: Int, y: Int): Boolean {
        return floorEffect!!.hasCell(x, y)
    }

    override fun clone(): Any {
        val effect = super.clone() as CellEffect
        effect.floorEffect = floorEffect
        return effect
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val effect = o as CellEffect
        return if (floorEffect != null) floorEffect == effect.floorEffect else effect.floorEffect == null
    }

    override fun hashCode(): Int {
        return floorEffect?.hashCode() ?: 0
    }

}