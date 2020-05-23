package com.wolg_vlad.dcrawler.ui.tiles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile

/**
 * Created by Voyager on 07.12.2017.
 */
class ColorTile @JvmOverloads constructor(color: Color, alpha: Float = getAlpha(color.a), filled: Boolean = true) : DungeonTile(null, 1, true, 1) {
    val iD = 1

    companion object {
        fun getAlpha(defaultAlpha: Float): Float {
            return if (defaultAlpha == 1f) {
                0.75f
            } else defaultAlpha
        }
    }

    init {
        color.a = alpha
        val pixmap = Pixmap(DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        if (filled) {
            pixmap.fill()
        } else {
            pixmap.drawRectangle(1, 1, DungeonTile.TILE_WIDTH - 2, DungeonTile.TILE_HEIGHT - 2)
        }
        val tileRegion = TextureRegion(Texture(pixmap))
        textureRegion = tileRegion
    }
}