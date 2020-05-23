package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.math.SumAction

/**
 * Created by Voyager on 19.11.2017.
 */
class BattlecryEffect(entity: Entity?, private val accuracyBonus: Int, private val damage: MathAction, private val turns: Int) : Effect(entity) {
    override val name = "Battle cry"
    override val description = "Increases accuracy for " + accuracyBonus + " and damage for " + damage.description + " for " + turns + " turns."
    override val icon = Texture("battle_cry.png")
    private val used = false
    override val positive: Boolean = true
    override val stackable: Boolean = false
    override val skillUse: Boolean = false
    override val stackSize: Int = 0
    override val expiring: Boolean = true
    override val expireTurns: Int = turns
    override val id: String = "main.dcrawler.effect.battlecry"

    override fun attackBonus(action: MathAction): MathAction {
        var action = action
        action = super.attackBonus(action)
        return SumAction(action, damage)
    }

    override fun startTurn() {
        super.startTurn()
        if (expireTurns == 0) {
            entity!!.accuracyBonus = entity!!.accuracyBonus - accuracyBonus
        }
    }

    init {
        entity!!.accuracyBonus = entity.accuracyBonus + accuracyBonus
    }
}