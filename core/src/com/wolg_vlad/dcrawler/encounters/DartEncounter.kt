package com.wolg_vlad.dcrawler.encounters

import com.wolg_vlad.dcrawler.effects.BloodiedEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.math.NumberAction

/**
 * Created by Voyager on 18.02.2018.
 */
class DartEncounter : Encounter() {
    override fun activate() {
        entity!!.addEffect(BloodiedEffect(entity, NumberAction(1), 3))
    }

    init {
        name = "Dart trap"
        text = "You was shoot by trap and now you bleed"
    }
}