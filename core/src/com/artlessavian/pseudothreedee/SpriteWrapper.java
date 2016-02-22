package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class SpriteWrapper implements Comparable<SpriteWrapper>
{
	public Sprite sprite;
	public Vector3 worldPos;
	private static Vector3 helper = new Vector3(0,0,0);

	public SpriteWrapper(Texture texture, Vector3 vector3)
	{
		sprite = new Sprite(texture);
		//sprite.setSize((float)Math.random(), (float)Math.random() * 16);
		worldPos = vector3;
		//worldPos.add(0,sprite.getHeight(),0);
	}

	@Override
	public int compareTo(SpriteWrapper o)
	{
		if (o.worldPos.y > this.worldPos.y)
		{
			return 1;
		} else if (o.worldPos.y < this.worldPos.y)
		{
			return -1;
		} else
		{
			// There's probably no way for this to be equal.
			return 0;
		}
	}

	private static final float magicNumber = Gdx.graphics.getHeight();

	public void draw(Batch batch, Camera camera, int initialHeight)
	{
		helper.x = worldPos.x;
		helper.y = worldPos.y;
		helper.z = worldPos.z;

		camera.project(helper);

		// An object [n] as close is [n] as big
		// An object [n] as far is 1/[n] as big
		float scale = 1 / (float)Math.sqrt(Math.pow((camera.position.y - worldPos.y),2) + camera.position.z * camera.position.z);

		// This statement protects against screen size changes.
		// I think. I dunno, it introduces some pseudo magic numbers :/
		scale *= (float)Gdx.graphics.getHeight() / (float)initialHeight;

		// Programming is fun
		// Especially linear algebra
		scale *= magicNumber;

		// OMG sprite.setScale is bad with positions ;___; so sad. wasted so much time.
		// Look at this jank
		float originalWidth = sprite.getWidth();
		float originalHeight = sprite.getHeight();
		sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);

		sprite.setCenterX(helper.x);
		sprite.setY(helper.y);
		sprite.draw(batch);

		sprite.setSize(originalWidth, originalHeight);
	}
}
