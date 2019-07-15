package com.sad.function.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sad.function.game.ShapeTest3;
import com.sad.function.game.ShapeTest5;
import com.sad.function.game.ShapeTest6;
import com.sad.function.game.ShapeTest7;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = true;
		config.foregroundFPS= 120;
		config.vSyncEnabled = false;
//		config.foregroundFPS= 0;
//		config.vSyncEnabled = false;
//		new LwjglApplication(new ApocalypticGame(), config);
//		new LwjglApplication(new ShapeTest5(), config);
//		new LwjglApplication(new ShapeTest6(), config);
//		new LwjglApplication(new ShapeTest7(), config);
	}
}
