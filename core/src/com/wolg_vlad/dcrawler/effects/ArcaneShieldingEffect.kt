package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 26.11.2017.
 */
class ArcaneShieldingEffect(entity: Entity?) : Effect(entity) {
    override val name = "God's protection"
    override val description = "God protects you from all damage for this turn"
    override val icon = Texture("gods_protection.png")
    override val positive: Boolean = true
    override val stackable: Boolean = false
    override val skillUse: Boolean = false
    override val stackSize: Int = 0
    override val expiring: Boolean = true
    override val id: String = "main.dcrawler.effect.arcane_shielding"
    override val expireTurns = 1

    override fun onDamage(damage: Int): Int {
        return 0
    }
}