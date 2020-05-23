package com.wolg_vlad.dcrawler

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.Enemy
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.event.EntityEvent
import com.wolg_vlad.dcrawler.event.EventAction
import com.wolg_vlad.dcrawler.event.EventController
import com.wolg_vlad.dcrawler.event.EventListener
import com.wolg_vlad.dcrawler.ui.screens.MainMenuScreen
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile
import java.util.*

class MyGdxGame : Game() {
    private var mainMenuScreen: MainMenuScreen? = null
    private var debugInfo = false
    protected var font: BitmapFont? = null
    protected var batch: SpriteBatch? = null
    override fun create() {
        font = BitmapFont()
        batch = SpriteBatch()
        EventListener.initListeners()
        EventController.addActionAfterEvent(object : EventAction() {
            override fun act(eventCode: String) {
                if (Arrays.asList<String>(*EntityEvent.eventCodes).contains(eventCode)) {
                    Entity.playingEntities.removeIf { entity: Entity -> !entity.isAlive }
                }
            }
        })
        DungeonTile.initTiles()
        Encounter.initEncouters()
        Enemy.createCRTable()
        input = InputMultiplexer()
        input!!.addProcessor(InputAdapter())
        //this.gameScreen = new GameScreen(this);
        mainMenuScreen = MainMenuScreen()
        setScreen(mainMenuScreen)
        input!!.addProcessor(object : InputAdapter() {
            override fun keyUp(keycode: Int): Boolean {
                println(keycode)
                if (keycode == Input.Keys.GRAVE) {
                    debugInfo = !debugInfo
                }
                if (keycode == Input.Keys.R) {
                    resize(1920, 1080)
                }
                return false
            }
        })
        Gdx.input.inputProcessor = input
    }

    override fun render() {
        super.render()
        if (debugInfo) {
            val h = Gdx.graphics.height.toFloat()
            batch!!.begin()
            font!!.draw(batch, "FPS: " + Gdx.graphics.framesPerSecond, 10f, h - 20)
            //font.draw(charBatch, "camera: " + camera.position, 10, h - 50); TODO draw in screen class
            batch!!.end()
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

    override fun pause() { // TODO Auto-generated method stub
    }

    override fun resume() { // TODO Auto-generated method stub
    }

    override fun dispose() { // TODO Auto-generated method stub
    }

    companion object {
        private var input: InputMultiplexer? = null
        fun setScreenInput(inputProcessor: InputProcessor?) {
            input!!.processors[0] = inputProcessor
        }
    }
}