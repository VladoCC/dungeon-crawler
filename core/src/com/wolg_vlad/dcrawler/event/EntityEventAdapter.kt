package com.wolg_vlad.dcrawler.event

import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 27.11.2017.
 */
/**
 * After every update of this class and Event interface, Enemy, DungeonCell and AI is need to be updated
 */
open class EntityEventAdapter : EntityEvent {
    override fun startTurn() {}
    override fun endTurn() {}
    override fun startMove() {}
    override fun endMove() {}
    override fun countMp(withMovement: Boolean): Int {
        return 0
    }

    override fun canUseSkill(): Boolean {
        return false
    }

    override fun startSkill() {}
    override fun endSkill() {}
    override fun attackBonus(action: MathAction): MathAction {
        return action
    }

    override fun healBonus(action: MathAction): MathAction {
        return action
    }

    override fun accuracyBonus(accuracy: Int, target: Entity): Int {
        return accuracy
    }

    override fun onDamage(damage: Int): Int {
        return damage
    }

    override fun onHeal(heal: Int): Int {
        return heal
    }

    override fun onEncounter(encounter: Encounter) {}
    override fun onDeath() {}
}