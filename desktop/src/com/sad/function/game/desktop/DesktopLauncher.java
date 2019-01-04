package com.sad.function.game.desktop;

import com.sad.function.input.BindingsLinker;

public class DesktopLauncher {
	public static void main (String[] arg) {
//		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		new LwjglApplication(new Game(), config);

		BindingsLinker linker = new BindingsLinker();
		linker.readBindings();

	}
}
