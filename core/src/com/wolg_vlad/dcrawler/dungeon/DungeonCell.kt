package com.wolg_vlad.dcrawler.dungeon

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.effects.CellEffect
import com.wolg_vlad.dcrawler.effects.Effect
import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.GraphStorage
import com.wolg_vlad.dcrawler.event.EntityEvent
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile

/**
 * Created by Voyager on 16.05.2017.
 */
class DungeonCell : TiledMapTileLayer.Cell, EntityEvent {
    var isOccupied = false
    var entity: Entity? = null
        set(value) {
        field = value
        for (effect in effects) {
            effect.entity = entity
        }}
    val effects = Array<CellEffect>()
    var x = 0
    var y = 0

    constructor() : super() {}
    constructor(tile: DungeonTile?) : super() {
        setTile(tile)
    }

    fun hasEntity(): Boolean {
        return entity != null
    }

    fun getEffect(index: Int): CellEffect? {
        return effects[index]
    }

    fun addEffect(effect: CellEffect?) {
        effects.add(effect)
        if (effect != null) {
            effect.entity = entity
        }
    }

    fun removeEffect(effect: CellEffect?) {
        effects.removeValue(effect, false)
    }

    fun onStepTo(x1: Int, y1: Int, x2: Int, y2: Int, entity: Entity) {
        for (effect in effects) {
            effect!!.stepToAction(x1, y1, x2, y2, entity)
        }
    }

    fun onStepFrom(x1: Int, y1: Int, x2: Int, y2: Int, entity: Entity) {
        for (effect in effects) {
            effect!!.stepFromAction(x1, y1, x2, y2, entity)
        }
    }

    override fun getTile(): DungeonTile {
        return super.getTile() as DungeonTile
    }

    override fun startTurn() {
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            effect!!.startTurn()
        }
    }

    override fun endTurn() {
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            effect!!.endTurn()
        }
    }

    override fun startMove() {
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            effect!!.startMove()
        }
    }

    override fun endMove() {
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            effect!!.endMove()
        }
    }

    override fun countMp(withMovement: Boolean): Int {
        var mp = 0
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            mp += effect!!.countMp(withMovement)
        }
        return mp
    }

    override fun canUseSkill(): Boolean {
        var use = false
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            if (effect!!.skillUse) {
                use = effect.canUseSkill()
            }
        }
        return use
    }

    override fun startSkill() {
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            effect!!.startSkill()
        }
    }

    override fun endSkill() {
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            effect!!.endSkill()
        }
    }

    override fun attackBonus(action: MathAction): MathAction {
        var action = action
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            action = effect!!.attackBonus(action)
        }
        return action
    }

    override fun healBonus(action: MathAction): MathAction {
        return action
    }

    override fun accuracyBonus(accuracy: Int, target: Entity): Int {
        var accuracy = accuracy
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            accuracy = effect!!.accuracyBonus(accuracy, target)
        }
        return accuracy
    }

    override fun onDamage(damage: Int): Int {
        var damage = damage
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            damage = effect!!.onDamage(damage)
        }
        return damage
    }

    override fun onHeal(heal: Int): Int {
        var heal = heal
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            heal = effect!!.onHeal(heal)
        }
        return heal
    }

    override fun onEncounter(encounter: Encounter) {
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            effect!!.onEncounter(encounter)
        }
    }

    override fun onDeath() {
        for (i in 0 until effects.size) {
            val effect: Effect? = effects[i]
            effect!!.onDeath()
        }
        isOccupied = false
        entity = null
        GraphStorage.createBottomGraph()
    }
}