package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.event.EntityEventAdapter
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 04.06.2017.
 */

//todo rework entity parameter for floor effects
abstract class Effect(entity: Entity?) : EntityEventAdapter(), Cloneable {
    var entity: Entity? = null
    //private set todo this can be private, but not for floor effects
    var hide = false
    protected set
    var delete = false
    protected set
    var checkMarker = 0 //todo this var doesn't make any sense
    private var turnPassed = 0

    abstract val icon: Texture?
    abstract val name: String
    abstract val description: String
    abstract val skillUse: Boolean

    abstract val positive: Boolean
    abstract val stackable: Boolean
    abstract val stackSize: Int
    abstract val expiring: Boolean
    abstract val id: String
    abstract val expireTurns: Int

    open fun onExpire() {}
    public override fun clone(): Any {
        val effect = super.clone() as Effect
        effect.delete = delete
        effect.hide = hide
        return effect
    }

    override fun equals(o: Any?): Boolean {
        if (o !is Effect) return false
        if (this === o) return true
        return false
    }

    /** Events  */
    fun adding() {}

    override fun startTurn() {
        super.startTurn()
    }

    override fun endTurn() {
        super.endTurn()
        println("$expireTurns abra-kad-abra")
        if (expiring) {
            if (turnPassed < expireTurns) {
                turnPassed++
            } else {
                delete = true
                onExpire()
            }
        }
    }

    override fun startMove() {
        super.startMove()
    }

    override fun endMove() {
        super.endMove()
    }

    override fun countMp(withMovement: Boolean): Int {
        super.countMp(withMovement)
        return 0
    }

    override fun canUseSkill(): Boolean {
        super.canUseSkill()
        return false
    }

    override fun startSkill() {
        super.startSkill()
    }

    override fun endSkill() {
        super.endSkill()
    }

    override fun attackBonus(action: MathAction): MathAction {
        super.attackBonus(action)
        return action
    }

    override fun onDamage(damage: Int): Int {
        super.onDamage(damage)
        return damage
    }

    override fun onHeal(heal: Int): Int {
        super.onHeal(heal)
        return heal
    }

    init {
        this.entity = entity
    }
}