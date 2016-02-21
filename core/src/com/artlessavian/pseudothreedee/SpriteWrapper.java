package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteWrapper
{
	public Sprite sprite;

	public SpriteWrapper(Texture texture)
	{
		sprite = new Sprite(texture);
	}
}
