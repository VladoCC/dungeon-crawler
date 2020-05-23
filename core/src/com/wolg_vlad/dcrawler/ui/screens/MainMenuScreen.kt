package com.wolg_vlad.dcrawler.ui.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.wolg_vlad.dcrawler.entities.Entity

class MainMenuScreen : AdvancedScreen() {
    private val atlas: TextureAtlas
    private val skin: Skin
    //private Table table;
    private val playButton: TextButton
    private val exitButton: TextButton
    private val buttonStyle: TextButtonStyle
    private val font: BitmapFont
    private val gameScreen: DungeonScreen? = null
    private val table: Table
    private val stage: Stage
    override fun render(delta: Float) { // TODO Auto-generated method stub
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) { // TODO Auto-generated method stub
    }

    override fun show() {
        playButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                input.removeProcessor(stage)
                Entity.playingEntities.removeIf { entity: Entity -> entity.isEnemy }
                (Gdx.app.applicationListener as Game).screen = CharGenScreen()
                //Gdx.graphics.setWindowedMode(1280, 720);
            }
        })
        exitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Gdx.app.exit()
            }
        })
        table.add(playButton).row()
        table.add(exitButton).row()
        table.setFillParent(true)
        stage.addActor(table)
        input.addProcessor(stage)
    }

    override fun hide() { // TODO Auto-generated method stub
        dispose()
    }

    override fun pause() { // TODO Auto-generated method stub
    }

    override fun resume() { // TODO Auto-generated method stub
    }

    override fun dispose() { // TODO Auto-generated method stub
        stage.dispose()
        skin.dispose()
    }

    init {
        stage = Stage()
        atlas = TextureAtlas("data/ui/ui.pack")
        font = BitmapFont()
        table = Table()
        table.debug()
        skin = Skin(atlas)
        table.skin = skin
        buttonStyle = TextButtonStyle()
        buttonStyle.up = skin.getDrawable("button_up")
        buttonStyle.down = skin.getDrawable("button_up")
        buttonStyle.font = font
        skin.add("default", buttonStyle)
        playButton = TextButton("Play", skin)
        exitButton = TextButton("Exit", skin)
    }
}