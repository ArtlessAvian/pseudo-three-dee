package com.artlessavian.pseudothreedee.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.artlessavian.pseudothreedee.TestTestTestTestTestTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.height = (int)(Math.random() * 1000) + 500;
		config.width = (int)(Math.random() * 1000) + 500;

		new LwjglApplication(new TestTestTestTestTestTest(), config);
	}
}
