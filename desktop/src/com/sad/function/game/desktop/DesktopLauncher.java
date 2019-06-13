package com.sad.function.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sad.function.game.ShapeTest3;
import com.sad.function.game.ShapeTest4;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = true;
//		new LwjglApplication(new ApocalypticGame(), config);
		new LwjglApplication(new ShapeTest4(), config);
//		new LwjglApplication(new ShapeTest(), config);
	}
}
