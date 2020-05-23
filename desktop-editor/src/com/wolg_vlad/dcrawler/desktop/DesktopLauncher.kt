package com.wolg_vlad.dcrawler.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.wolg_vlad.dcrawler.LevelManager

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = 1280
        config.height = 720
        LwjglApplication(LevelManager(), config)
    }
}