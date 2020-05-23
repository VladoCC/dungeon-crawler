package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 04.06.2017.
 */
class AttackEffect(entity: Entity?) : Effect(entity) {
    override val name = "Attack"
    override val description = "You may attack or make second move in this turn."
    override val icon = Texture("sword.png")
    private var used = false
    override val positive: Boolean = true
    override val skillUse: Boolean = true
    override val stackable: Boolean = false
    override val stackSize: Int = 1
    override val expiring: Boolean = false
    override val expireTurns: Int = 0
    override val id: String = "main.dcrawler.effect.attack"

    override fun startMove() {
        if (entity!!.isMoved) {
            entity!!.addMp(entity!!.speed)
            used = true
        }
        if (entity!!.isMoved) {
            delete = true
            Entity.updateSkills()
        }
    }

    override fun endMove() {}
    override fun countMp(withMovement: Boolean): Int {
        if (entity != null) {
            if (entity!!.isMoved && !delete && !used) { //TODO don't work if starting mp is 0
                if (!entity!!.isMovement) {
                    println("Adding MP")
                    hide = entity?.path != null
                    return entity!!.speed
                }
            } else if (entity!!.isMoved && !withMovement && !delete && !used) {
                if (!entity!!.isMovement) {
                    return entity!!.speed
                }
            } else if (hide && !entity!!.isMovement) {
                hide = false
            }
        }
        return 0
    }

    override fun canUseSkill(): Boolean {
        return !delete
    }

    override fun endSkill() {
        delete = true
        Entity.updateSkills()
    }

    override fun endTurn() {
        delete = true
    }
}