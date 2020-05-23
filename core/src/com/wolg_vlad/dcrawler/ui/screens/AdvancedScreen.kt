package com.wolg_vlad.dcrawler.ui.screens

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.wolg_vlad.dcrawler.MyGdxGame

/**
 * Created by Voyager on 03.04.2018.
 */
abstract class AdvancedScreen : Screen {
    val input: InputMultiplexer

    init {
        input = InputMultiplexer()
        MyGdxGame.setScreenInput(input)
    }
}