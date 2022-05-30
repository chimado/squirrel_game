package com.mygdx.squirrel_game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.squirrel_game.squirrel_game;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Squirrel Game");
		//config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode()); enable if you want exclusive fullscreen
		config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width, Lwjgl3ApplicationConfiguration.getDisplayMode().height);
		config.setWindowIcon("bald_eagle1.png");
		new Lwjgl3Application(new squirrel_game(), config);
	}
}