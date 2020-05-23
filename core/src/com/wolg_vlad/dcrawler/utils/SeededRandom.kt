package com.wolg_vlad.dcrawler.utils

import com.badlogic.gdx.Gdx
import java.util.*

object SeededRandom {
    var seed: Long = 0
    private var random: Random
    val instance: Random?
        get() = getInstance()

    fun getInstance(seed: Long = this.seed): Random {
        if (seed != SeededRandom.seed) {
            Gdx.app.debug("random seed", "" + seed)
            random!!.setSeed(seed)
        }
        return random
    }

    init {
        seed = Random().nextLong()
        Gdx.app.debug("random seed", "" + seed)
        random = Random(seed)
    }
}