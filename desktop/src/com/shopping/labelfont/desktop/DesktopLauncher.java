package com.shopping.labelfont.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.shopping.Base.Base;
import com.badlogic.gdx.Files;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		config.resizable = false;
		config.title = "STARBUGS Alpha 2.0";
		config.addIcon("assets/icon/WholeCat.png", Files.FileType.Internal);
		new LwjglApplication(new Base(), config);
	}
}
