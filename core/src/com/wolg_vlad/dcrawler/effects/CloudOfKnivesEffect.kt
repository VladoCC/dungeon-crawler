package com.wolg_vlad.dcrawler.effects

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 29.11.2017.
 */
class CloudOfKnivesEffect(floorEffect: FloorEffect?, private val damage: MathAction?) : CellEffect(floorEffect) {
    override val name = "Cloud of knives"
    override val description = ""
    override val icon = Texture("cloud_knives.png")
    override val positive: Boolean = false
    override val skillUse: Boolean = false
    override val stackable: Boolean = false
    override val stackSize: Int = 0
    override val expiring: Boolean = false
    override val id: String = "main.dcrawler.effect.cloud_of_knives"
    override val expireTurns: Int = 0

    override fun onStepTo(entity: Entity) {
        super.onStepTo(entity)
        entity.addHp(-damage!!.act())
    }

    override fun startTurn() {
        super.startTurn()
        val dmg = -damage!!.act()
        entity!!.addHp(dmg)
    }
}