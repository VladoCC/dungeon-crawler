package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity

class DisarmedEffect(entity: Entity?, private val turns: Int) : Effect(entity) {
    override val name = "Disarmed"
    override val description = "You can not attack for " + turns + "turn(-s)"
    override val icon = Texture("disarm.png")
    override val positive: Boolean = false
    override val skillUse: Boolean = true
    override val stackable: Boolean = false
    override val stackSize: Int = 0
    override val expiring: Boolean = true
    override val expireTurns: Int = 1
    override val id: String = "main.dcrawler.effect.disarmed"

    override fun canUseSkill(): Boolean {
        return false
    }
}