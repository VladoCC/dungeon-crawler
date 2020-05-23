package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture

/**
 * Created by Voyager on 29.11.2017.
 */
class FloorClearingEffect(effect: FloorEffect, turns: Int) : Effect(null) {
    override val name = ""
    override val description = ""
    override val icon: Texture = Texture("bomb.png")
    override val positive: Boolean = true
    override val skillUse: Boolean = false
    override val stackable: Boolean = true
    override val stackSize: Int = 4096
    override val expiring: Boolean = true
    override val expireTurns: Int = turns
    override val id: String = "main.dcrawler.effect.floor_clearing"
    private var effect: FloorEffect

    override fun onExpire() {
        super.onExpire()
        effect.removeEffect(true)
    }

    init {
        hide = true
        this.effect = effect
    }
}