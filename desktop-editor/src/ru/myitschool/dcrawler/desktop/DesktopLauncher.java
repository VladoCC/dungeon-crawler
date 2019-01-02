package ru.myitschool.dcrawler.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.myitschool.dcrawler.LevelManager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 680;
        config.width = 1200;
        config.resizable = false;
		config.samples=10;
		new LwjglApplication(new LevelManager(), config);
	}
}
