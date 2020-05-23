package com.wolg_vlad.dcrawler.math

/**
 * Created by Voyager on 04.08.2017.
 */
abstract class MathAction {
    abstract fun act(): Int
    abstract val description: String?
    open fun max(): Int {
        return act()
    }

    open fun min(): Int {
        return act()
    }
}