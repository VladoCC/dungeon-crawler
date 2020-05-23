package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 18.04.2018.
 */
class MinAction(private vararg val actions: MathAction) : MathAction() {


    override fun act(): Int {
        var min = Int.MAX_VALUE
        for (i in actions.indices) {
            val num = actions[0].act()
            if (num < min) {
                min = num
            }
        }
        return min
    }

    override val description: String?
        get() {
            var description = "min(" + actions[0].description
            for (i in 1 until actions.size) {
                description += ", " + actions[i]
            }
            description += ")"
            return description
        }
}