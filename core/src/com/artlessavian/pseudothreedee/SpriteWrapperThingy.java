package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class SpriteWrapperThingy
{
	public LinkedList<SpriteWrapper> spriteWrappers;
	private final int initHeight;

	public SpriteWrapperThingy()
	{
		spriteWrappers = new LinkedList<SpriteWrapper>();
		initHeight = Gdx.graphics.getHeight();
	}

	public void add(SpriteWrapper s)
	{
		spriteWrappers.add(s);
	}

	public void draw(SpriteBatch batch, Camera camera)
	{
		Collections.sort(spriteWrappers);
		for (SpriteWrapper s : spriteWrappers)
		{
			if (s.worldPos.y < camera.position.y) {break;}
			s.draw(batch, camera, initHeight);
		}
	}
}
