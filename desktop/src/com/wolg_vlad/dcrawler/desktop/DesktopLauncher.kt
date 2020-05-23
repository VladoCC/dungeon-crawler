package com.wolg_vlad.dcrawler.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.wolg_vlad.dcrawler.MyGdxGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setTitle("Dungeon Crawler")
        val monitors = Lwjgl3ApplicationConfiguration.getMonitors()
        //config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode(monitors[1]));
        config.setWindowedMode(1280, 720)
        Lwjgl3Application(MyGdxGame(), config)
    }
}