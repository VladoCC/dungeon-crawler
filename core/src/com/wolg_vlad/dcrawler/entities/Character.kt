package com.wolg_vlad.dcrawler.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array

/**
 * Created by Voyager on 25.04.2017.
 */
class Character(texture: Texture, portrait: Texture, x: Float, y: Float, hp: Int, hpMax: Int, mp: Int, mpMax: Int, armor: Int) : Entity(texture, portrait, x, y, hp, hpMax, mp, mpMax, armor) {
    private var moved = false
    override val isCharacter: Boolean
        get() = true

    override fun turnIsEnded(): Boolean {
        return false
    }

    override fun endTurn() {
        super.endTurn()
    }

    override fun startTurn() {
        moved = false
        super.startTurn()
    }

    override fun startMove() {
        super.startMove()
    }

    override fun endMove() {
        moved = true
        super.endMove()
        setMp(0)
    }

    companion object {
        private const val CHARS_MAX = 4
        var created = false
        val chars = Array<Character>(CHARS_MAX)
        fun getChar(num: Int): Character {
            return chars[num]
        }

    }

    init {
        chars.add(this)
        add(this)
    }
}