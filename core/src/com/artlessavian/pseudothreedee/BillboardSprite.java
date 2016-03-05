package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class BillboardSprite implements Comparable<BillboardSprite>
{
	private BillboardManager owner;

	public Sprite sprite;
	public Sprite shadow;

	private float width;
	public Vector3 worldPos; // Actually the position of the "feet"

	private static Vector3 helper = new Vector3(0, 0, 0);

	public BillboardSprite(Texture sprite, Texture shadow, float width, Vector3 vector3)
	{
		this.worldPos = vector3;

		this.sprite = new Sprite(sprite);
		this.shadow = new Sprite(shadow);
		this.width = width;

		this.sprite.setSize(width, this.sprite.getHeight() * width / this.sprite.getWidth());
	}

	public void setOwner(BillboardManager owner)
	{
		this.owner = owner;
	}

	@Override
	public int compareTo(BillboardSprite o)
	{
		if (!this.owner.equals(o.owner))
		{
			throw new UnsupportedOperationException("BillboardSprite owners should be the same");
		}

		PerspectiveCamera camera = owner.camera;

		// This will break with a cam angle pointed straight down.
		helper.x = o.worldPos.x - camera.position.x;
		helper.y = o.worldPos.y - camera.position.y;
		helper.z = camera.position.z;
		float oDist = helper.dot(camera.direction);

		helper.x = this.worldPos.x - camera.position.x;
		helper.y = this.worldPos.y - camera.position.y;
		helper.z = camera.position.z;
		float thisDist = helper.dot(camera.direction);

		// Sort to render in the correct order
		if (thisDist < oDist)
		{
			return 1;
		} else if (thisDist > oDist)
		{
			return -1;
		} else
		{
			// There's probably no way for this to be equal.
			return 0;
		}
	}

	private static final float magicNumberScaling = 58 / 48f;

	public void drawSprite(Batch batch)
	{
		// Vector Magic
		helper.x = worldPos.x - owner.camera.position.x;
		helper.y = worldPos.y - owner.camera.position.y;
		helper.z = worldPos.z - owner.camera.position.z;
		float distance = helper.dot(owner.camera.direction);

		if (distance < owner.nearInvisibleDistance) {return;} // More Culling && Covers things behind camera
		else if (distance > owner.farInvisibleDistance) {return;} // More Culling
		else if (distance < owner.nearBeginFadeDistance)
		{
			sprite.setAlpha(Math.max(0, (distance - owner.nearInvisibleDistance) / (owner.nearBeginFadeDistance - owner.nearInvisibleDistance)));
		} else if (distance > owner.farBeginFadeDistance)
		{
			sprite.setAlpha(Math.max(0, (distance - owner.farInvisibleDistance) / (owner.farBeginFadeDistance - owner.farInvisibleDistance)));
		} else {sprite.setAlpha(1);}

		// The concept here is guaranteed.
		// An object [n] as close is [n] as big
		// An object [n] as far is 1/[n] as big
		// However, it scales by distance to the feet, which causes odd distortions.
		float scale = 1 / distance;

		// i hate trial and error
		scale *= 45 / owner.camera.fieldOfView;
		scale *= (float)Gdx.graphics.getHeight();

		// brute forced number
		scale *= magicNumberScaling;

		// sprite.setScale actually scales every property, like distance from origin.
		// oops.
		float originalHeight = sprite.getHeight();
		sprite.setSize(width * scale, originalHeight * scale);

		helper.x = worldPos.x;
		helper.y = worldPos.y;
		helper.z = worldPos.z;
		owner.camera.project(helper);

		sprite.setCenterX(helper.x);
		sprite.setY(helper.y);

		sprite.draw(batch);

		sprite.setSize(width, originalHeight);
	}

	public void drawShadow(SpriteBatch batch)
	{
		// Culling
		helper.x = worldPos.x;
		helper.y = worldPos.y;
		helper.z = 0;
		if (helper.sub(owner.camera.position).hasOppositeDirection(owner.camera.direction)) {return;}

		shadow.setCenter(worldPos.x, worldPos.y);
		shadow.setSize(this.width, this.width);
		// Needs tweaking.
		// This assumes that worldPos.z will never be negative though.
		shadow.setAlpha((float)Math.pow(1 / 2f, 1 / 20f * worldPos.z));
		shadow.draw(batch);
	}
}
