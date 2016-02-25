package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Collections;
import java.util.LinkedList;

public class BillboardManager
{
	public LinkedList<BillboardSprite> billboards;
	Camera camera;
	OrthographicCamera oneToOne;
	public int warningUnspammer = 0;

	final float nearBeginFadeDistance = 100;
	final float nearInvisibleDistance = 50;
	final float farBeginFadeDistance = 2000;
	final float farInvisibleDistance = 3000;

	public BillboardManager(Camera persp, OrthographicCamera ortho)
	{
		billboards = new LinkedList<BillboardSprite>();
		camera = persp;
		oneToOne = ortho;
	}

	public void add(BillboardSprite s)
	{
		s.setOwner(this);
		billboards.add(s);
	}


	public void draw(SpriteBatch batch)
	{
		if (Math.abs(camera.direction.x) < 0.00001 && Math.abs(camera.direction.y) < 0.00001)
		{
			if (warningUnspammer == 0) {System.err.println("[BillboardSprite] Don't point the camera downish.");}
			warningUnspammer = (warningUnspammer + 1) % 30; // Once every half second?
		}

		// Get render order
		Collections.sort(billboards);

		for (BillboardSprite b : billboards)
		{
			batch.setProjectionMatrix(camera.combined);
			b.drawShadow(batch);
		}

		for (BillboardSprite b : billboards)
		{
			batch.setProjectionMatrix(oneToOne.combined);
			b.drawSprite(batch);
		}
	}
}
