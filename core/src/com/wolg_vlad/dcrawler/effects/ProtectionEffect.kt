package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 05.11.2017.
 */
class ProtectionEffect(entity: Entity, armor: Int) : Effect(entity) {
    override val name = "Armor Improvement"
    override val description = "Increases armor for $armor until end of next turn."
    override val icon = Texture("sword.png")
    override val positive: Boolean = true
    override val skillUse: Boolean = false
    override val stackable: Boolean = false
    override val stackSize: Int = 0
    override val expiring: Boolean = false //todo i'm not sure if it's right behavior
    override val expireTurns: Int = 0
    override val id: String = "main.dcrawler.effect.protection"
    private val armor = 0

    override fun endTurn() {
        super.endTurn()
        if (expireTurns == 0) {
            entity!!.armor = entity!!.armor - armor
        }
    }

    init {
        entity.armor = entity.armor + armor
    }
}