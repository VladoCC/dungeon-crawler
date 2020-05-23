package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 04.08.2017.
 */
class SumAction(private vararg val actions: MathAction) : MathAction() {

    override fun act(): Int {
        var sum = 0
        for (i in actions.indices) {
            sum += actions[i].act()
        }
        return sum
    }

    override val description: String?
        get() {
            var description = "" + actions[0].description
            for (i in 1 until actions.size) {
                description += " + " + actions[i].description
            }
            return description
        }

    override fun max(): Int {
        var sum = 0
        for (i in actions.indices) {
            sum += actions[i].max()
        }
        return sum
    }

    override fun min(): Int {
        var sum = 0
        for (i in actions.indices) {
            sum += actions[i].min()
        }
        return sum
    }
}