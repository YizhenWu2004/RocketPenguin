package com.raccoon.mygame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.raccoon.mygame.game.GDXRoot;
import com.raccoon.mygame.game.LePetitRaccoon;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1920, 1080);
		config.setTitle("RocketPenguin");
		new Lwjgl3Application(new GDXRoot(), config);
	}
}