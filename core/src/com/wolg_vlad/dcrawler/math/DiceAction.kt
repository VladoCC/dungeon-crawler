package com.wolg_vlad.dcrawler.math

import java.util.*

/**
 * Created by Voyager on 04.08.2017.
 */
class DiceAction(private val dice: Int) : MathAction() {
    private val random: Random
    override fun act(): Int {
        var result = 0
        result += random.nextInt(dice) + 1
        return result
    }

    override val description: String?
        get() = "d$dice"

    override fun max(): Int {
        return dice
    }

    override fun min(): Int {
        return 1
    }

    init {
        random = Random()
    }
}