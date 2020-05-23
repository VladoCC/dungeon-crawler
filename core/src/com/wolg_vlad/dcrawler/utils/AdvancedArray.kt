package com.wolg_vlad.dcrawler.utils

/**
 * Array with additional functions of clipping, getting random element and stack/queue-style getters
 */
class AdvancedArray<T> : com.badlogic.gdx.utils.Array<T>() {
    fun clip(from: Int, to: Int): Boolean {
        if (from >= 0 && to < items.size && from <= to) {
            val objects = arrayOfNulls<Any>(to - from + 1) as Array<T?>
            for (i in 0 until to - from + 1) {
                objects[i] = items[from + i]
            }
            items = objects
            size = objects.size
            return true
        }
        return false
    }

    val first: T
        get() = items[0]

    val last: T
        get() = items[size - 1]

    val random: T
        get() {
            val random = SeededRandom.getInstance()!!.nextInt(size)
            return items[random]
        }
}