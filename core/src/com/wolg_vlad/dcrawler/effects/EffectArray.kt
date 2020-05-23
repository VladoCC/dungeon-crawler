package com.wolg_vlad.dcrawler.effects

/**
 * Created by Voyager on 04.08.2017.
 */
class EffectArray : ArrayList<Effect>() {
    var id = 0 //TODO reset after exiting of dungeon
    override fun add(effect: Effect): Boolean {
        add(size, effect)
        return true
    }

    override fun add(pos: Int, effect: Effect) {
        var pos = pos
        val initialSize = super.size
        if (effect.stackable) {
            val stack = effect.stackSize - 1
            var count = 0
            for (i in initialSize - 1 downTo 0) {
                val oldEffet = get(i)
                if (oldEffet.id == effect.id) { //todo rework this to remove oldest stack or find another way to work around it
                    count++
                    if (count >= stack) {
                        removeAt(i)
                    }
                }
            }
        } else {
            for (i in initialSize - 1 downTo 0) {
                val oldEffet = get(i)
                if (oldEffet.id == effect.id) {
                    removeAt(i)
                }
            }
        }
        if (pos > size) {
            pos = size
        }
        super.add(pos, effect)
        effect.checkMarker = id
        id++
    }

    fun removeId(id: Int) {
        var i = 0
        while (i < size) {
            val effect = get(i)
            if (effect.checkMarker == id) {
                removeAt(i)
                i--
            }
            i++
        }
    }
}