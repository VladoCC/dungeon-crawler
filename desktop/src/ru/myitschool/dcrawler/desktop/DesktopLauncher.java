package ru.myitschool.dcrawler.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.myitschool.dcrawler.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Dungeon Crawler");
		Graphics.Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();
		//config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode(monitors[1]));
		config.setWindowedMode(1280, 720);
		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
