package com.mygdx.squirrel_game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.squirrel_game.squirrel_game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1280, 800);
		config.setTitle("Squirrel Game");
		new Lwjgl3Application(new squirrel_game(), config);
	}
}
