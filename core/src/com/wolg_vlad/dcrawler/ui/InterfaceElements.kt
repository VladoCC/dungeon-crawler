package com.wolg_vlad.dcrawler.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

/**
 * Created by Voyager on 29.05.2017.
 */
object InterfaceElements {
    private const val ANIM_DURATION = 0.2f
    private const val TEXTURE_WIDTH = 32
    private const val TEXTURE_HEIGHT = 32
    private var health: Animation<TextureRegion>
    private var moves: Animation<TextureRegion>
    init {
        val hpAnim = TextureRegion.split(Texture("health.png"), TEXTURE_WIDTH, TEXTURE_HEIGHT)
        val regions = Array<TextureRegion>()
        for (element in hpAnim[0]) {
            regions.add(element)
        }
        health = Animation(ANIM_DURATION, regions)
        health.setPlayMode(Animation.PlayMode.LOOP)
        val moveAnim = TextureRegion.split(Texture("speed.png"), TEXTURE_WIDTH, TEXTURE_HEIGHT)
        val regs = Array<TextureRegion>()
        for (i in 0 until moveAnim[0].size) {
            regs.add(moveAnim[0][i])
        }
        moves = Animation(ANIM_DURATION, regs)
        moves.setPlayMode(Animation.PlayMode.LOOP)
    }

    fun getHealth(): TextureRegion {
        return getHealth(0f)
    }

    fun getHealth(stateTime: Float): TextureRegion {
        return health!!.getKeyFrame(stateTime) as TextureRegion
    }

    fun getMoves(): TextureRegion {
        return getMoves(0f)
    }

    fun getMoves(stateTime: Float): TextureRegion {
        return moves!!.getKeyFrame(stateTime) as TextureRegion
    }
}