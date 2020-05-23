package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 16.11.2017.
 */
class BloodiedEffect(entity: Entity?, private val damage: MathAction, private val rounds: Int) : Effect(entity) {
    override val name = "Bloodied"
    override var description = "Deal " + damage.description + " damage on start of your turn for " + rounds + " turns."
    override val icon = Texture("blood.png") //TODO change to better icon
    private val used = false
    override val positive: Boolean = false
    override val skillUse: Boolean = false
    override val stackable: Boolean = false
    override val stackSize: Int = 1
    override val expiring: Boolean = true
    override val id: String = "main.dcrawler.effect.bloodied"
    override val expireTurns: Int = rounds

    override fun startTurn() {
        super.startTurn()
        if (!delete) {
            description += "Deal " + damage.description + " damage on start of your turn for " + expireTurns + " turns."
            entity!!.addHp(-damage.act())
        }
    }

}