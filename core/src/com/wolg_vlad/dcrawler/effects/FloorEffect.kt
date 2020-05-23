package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.wolg_vlad.dcrawler.dungeon.DungeonCell
import com.wolg_vlad.dcrawler.dungeon.DungeonMap
import com.wolg_vlad.dcrawler.dungeon.DungeonMap.Companion.getCell
import com.wolg_vlad.dcrawler.entities.skills.Target

/**
 * Created by Voyager on 29.11.2017.
 */
class FloorEffect @JvmOverloads constructor(cells: MutableList<Target>, effect: CellEffect, color: Color, show: Boolean, activate: Boolean = true) {
    var cells = mutableListOf<DungeonCell>()
    set(value) {
        field = value
        DungeonMap.updateEffects()
    }
    var nullCells = mutableListOf<Target>()
    var color: Color
    var isShow = true
    var activate = true
    var effect: CellEffect

    constructor(cells: MutableList<Target>, effect: CellEffect, show: Boolean, activate: Boolean) : this(cells, effect, Color.BLACK, show, activate) {}
    constructor(cells: MutableList<Target>, effect: CellEffect, activate: Boolean) : this(cells, effect, Color.BLACK, false, activate) {}

    fun activate() {
        if (!activate) {
            activate = true
            val newCells = mutableListOf<Target>()
            for (i in 0 until nullCells.size) {
                val cell = nullCells[i]
                if (!addCell(cell)) {
                    newCells.add(cell)
                }
            }
            nullCells = newCells
        }
        effects.add(this)
        DungeonMap.updateEffects()
    }

    fun addCell(target: Target): Boolean {
        val cell = getCell(target.x, target.y)
        if (cell != null) {
            cells.add(cell)
            cell.addEffect(effect.clone() as CellEffect)
            return true
        }
        return false
    }

    fun updateCells(movement: Vector2) {
        for (i in 0 until nullCells.size) {
            val target = nullCells[i]
            target.move(movement)
        }
        nullCells.removeIf { addCell(it) }
    }

    fun removeEffect(update: Boolean) {
        for (cell in cells) {
            cell.removeEffect(effect)
        }
        cells.clear()
        isShow = false
        if (update) {
            DungeonMap.updateEffects()
        }
    }

    fun hasCell(x: Int, y: Int): Boolean {
        val cell = getCell(x, y)
        for (thisCell in cells) {
            if (cell == thisCell) {
                return true
            }
        }
        return false
    }

    companion object {
        val effects = mutableListOf<FloorEffect>()
        fun updateEffects(movements: Vector2) {
            for (effect in effects) {
                effect.updateCells(movements)
            }
            DungeonMap.updateEffects()
        }

        fun clearEffects() {
            for (effect in effects) {
                effect.removeEffect(false)
            }
            effects.clear()
        }

    }

    init {
        effect.floorEffect = this
        this.effect = effect
        for (cell in cells!!) {
            if (!activate || activate && !addCell(cell)) {
                nullCells.add(cell)
            }
        }
        this.activate = activate
        this.color = color
        isShow = show
        if (activate) {
            effects.add(this)
            DungeonMap.updateEffects()
        }
    }
}