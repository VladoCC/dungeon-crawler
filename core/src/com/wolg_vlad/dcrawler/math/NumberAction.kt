package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 04.08.2017.
 */
class NumberAction(private val result: Int) : MathAction() {
    override fun act(): Int {
        return result
    }

    override val description: String?
        get() = "" + result

}