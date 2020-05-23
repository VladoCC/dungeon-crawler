package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 24.11.2017.
 */
class ControlledEffect(entity: Entity?) : Effect(entity) {
    private val used = false
    override val name = "Mind control"
    override val description = "Someone else controls your mind for this turn."
    override val icon = Texture("mind_control.png")
    override val positive: Boolean = false
    override val skillUse: Boolean = false
    override val stackable: Boolean = false
    override val stackSize: Int = 0
    override val expiring: Boolean = true
    override val expireTurns: Int = 1
    override val id: String = "main.dcrawler.effect.controlled"

    override fun endTurn() {
        super.endTurn()
        entity!!.isControlled = false
    }

    init {
        entity!!.isControlled = true
    }
}