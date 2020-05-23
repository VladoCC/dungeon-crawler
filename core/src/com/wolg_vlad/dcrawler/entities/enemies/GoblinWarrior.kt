package com.wolg_vlad.dcrawler.entities.enemies

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Enemy
import com.wolg_vlad.dcrawler.entities.ai.AI
import com.wolg_vlad.dcrawler.entities.ai.GoblinAI

/**
 * Created by Voyager on 11.08.2017.
 */
class GoblinWarrior(x: Float, y: Float) : Enemy(Texture("sprites/goblinwarrior.png"), Texture("goblin.png"), x, y) {
    override fun createAI(): AI {
        return GoblinAI(this)
    }

    init {
        challengeRating = 1f
        hp = 10
        hpMax = 10
        setMp(6)
        speed = 6
        armor = 5
    }
}