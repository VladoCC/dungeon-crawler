package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 18.04.2018.
 */
class MaxAction(private vararg val actions: MathAction) : MathAction() {

    override fun act(): Int {
        var max = Int.MIN_VALUE
        for (i in actions.indices) {
            val num = actions[0].act()
            if (num > max) {
                max = num
            }
        }
        return max
    }

    override val description: String?
        get() {
            var description = "max(" + actions[0].description
            for (i in 1 until actions.size) {
                description += ", " + actions[i]
            }
            description += ")"
            return description
        }

}