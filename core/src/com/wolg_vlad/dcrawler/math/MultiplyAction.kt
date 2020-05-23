package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 04.08.2017.
 */
class MultiplyAction(private vararg val actions: MathAction) : MathAction() {

    override fun act(): Int {
        var mult = 1
        for (i in actions.indices) {
            mult *= actions[i].act()
        }
        return mult
    }

    override val description: String?
        get() {
            var description = "(" + actions[0].description + ")"
            for (i in 1 until actions.size) {
                description += " * " + "(" + actions[i].description + ")"
            }
            return description
        }

    override fun max(): Int {
        var mult = 1
        for (i in actions.indices) {
            mult *= actions[i].max()
        }
        return mult
    }

    override fun min(): Int {
        var mult = 1
        for (i in actions.indices) {
            mult *= actions[i].min()
        }
        return mult
    }
}