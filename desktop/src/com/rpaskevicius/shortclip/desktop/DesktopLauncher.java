package com.rpaskevicius.shortclip.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rpaskevicius.shortclip.ShortClip;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.samples = 2;

		config.height = 1080 / 3;
		config.width = 1920 / 3;

		new LwjglApplication(new ShortClip(), config);
	}
}
