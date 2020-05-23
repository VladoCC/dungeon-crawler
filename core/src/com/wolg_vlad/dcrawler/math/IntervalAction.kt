package com.wolg_vlad.dcrawler.math

import java.util.*

/**
 * Created by Voyager on 05.08.2017.
 */
class IntervalAction(private val min: Int, private val max: Int) : MathAction() {
    private val random: Random
    override fun act(): Int {
        return random.nextInt(max + 1 - min) + min
    }

    override val description: String?
        get() = "($min-$max)"

    override fun max(): Int {
        return max
    }

    override fun min(): Int {
        return min
    }

    init {
        random = Random()
    }
}