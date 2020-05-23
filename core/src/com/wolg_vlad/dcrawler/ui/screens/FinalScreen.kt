package com.wolg_vlad.dcrawler.ui.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.wolg_vlad.dcrawler.effects.FloorEffect
import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 02.04.2018.
 */
class FinalScreen(private val text: String) : AdvancedScreen() {
    private var time = 0f
    private var stage: Stage? = null
    override fun show() {
        stage = Stage()
        val generator = FreeTypeFontGenerator(Gdx.files.internal("pixelart.otf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 24
        val font = generator.generateFont(parameter)
        val style = LabelStyle(font, Color.WHITE)
        val label = Label(text, style)
        val container = Container(label)
        container.center()
        container.setPosition(0f, 0f)
        container.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        stage!!.addActor(container)
        FloorEffect.clearEffects()
        for (entity in Entity.playingEntities) {
            entity.clearEffects()
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage!!.draw()
        time += delta
        if (time >= 3.5f) {
            (Gdx.app.applicationListener as Game).screen = MainMenuScreen()
        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}

}