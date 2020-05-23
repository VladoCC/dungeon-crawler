package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 28.11.2017.
 */
class MarkedEffect(entity: Entity?, private val marker: Entity?, //penalty is negative number
                   private val penalty: Int) : Effect(entity) {
    override val name = "Marked"
    override val description = "All your attacks except of attacks to creature that marked you gets $penalty accuracy penalty."
    override val icon = Texture("mark.png")
    override val positive: Boolean = false
    override val skillUse: Boolean = false
    override val stackable: Boolean = false
    override val stackSize: Int = 0
    override val expiring: Boolean = false
    override val expireTurns: Int = 0
    override val id: String = "main.dcrawler.effect.marked"

    override fun accuracyBonus(accuracy: Int, target: Entity): Int {
        var accuracy = accuracy
        if (target !== marker) {
            accuracy += penalty
        }
        return accuracy
    }
}