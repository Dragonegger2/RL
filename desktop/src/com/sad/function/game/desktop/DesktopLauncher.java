package com.sad.function.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sad.function.game.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = true;
		config.foregroundFPS= 120;
		config.vSyncEnabled = false;
//		config.foregroundFPS= 0;
//		config.vSyncEnabled = false;
		new LwjglApplication(new Tower(), config);
	}
}
