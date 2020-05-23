package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 02.12.2017.
 */
class ImmobilizedEffect(entity: Entity?, turns: Int) : Effect(entity) {
    override val name = "Immobilized"
    override val description = "You can not move for $turns turn(-s)"
    override val icon = Texture("immobilization.png")
    override val positive: Boolean = false
    override val skillUse: Boolean = false
    override val stackable: Boolean = false
    override val stackSize: Int = 0
    override val expiring: Boolean = true
    override val expireTurns: Int = turns
    override val id: String = "main.dcrawler.effect.immobilized"

    override fun onExpire() {
        super.onExpire()
        entity!!.isImmobilized = false
    }

    init {
        entity!!.isImmobilized = true
    }
}