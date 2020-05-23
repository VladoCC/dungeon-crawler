package com.wolg_vlad.dcrawler

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import java.util.*

/**
 * Created by КАРАТ on 21.03.2017.
 */
/**
 * Это основной класс, который заведует всеми уровнями
 * система как в Unity со сценами: каждый Screen - "сцена"
 * небольшой TODO : сделать суперкласс для классов "сцены"
 *
 * Метод StartLevel запускает уровень, который ему скормили
 */
class LevelManager : Game(), ApplicationListener {
    override fun create() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("10771.ttf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 16
        //parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэяю1234567890-_/.,:!@#$%^&*()+[]=?<>{}";
        mainFont = generator.generateFont(parameter)
        startLev(StartScreen())
    }

    fun startLev(sc: Screen?) {
        setScreen(sc)
    }

    companion object {
        var mainFont: BitmapFont? = null
        fun setAlphaAll(sl: ArrayList<Sprite>, am: Float) {
            for (s in sl) {
                s.setAlpha(am)
            }
        }
    }
}