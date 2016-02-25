package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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
	private static Vector3 helper2 = new Vector3(0, 0, 0);

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

		Camera camera = owner.camera;

		// This will break with a cam angle pointed straight down.
		// Then, nothing is comparable :/
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

		// There's probably some vector quaternion matrix shenanigans that makes this work out.
		// However, the height seems to work, for any resolution, with the camera resolution as 45 :/
		// I thought it would be the end all magic number, but nope.
		scale *= (float)Gdx.graphics.getHeight();

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

		if (false) // TODO: Sprites rotate to reflect their z axis
		{
//			helper2.x = worldPos.x;
//			helper2.y = worldPos.y;
//			helper2.z = worldPos.z + 1;
//			owner.camera.project(helper2);
//
//			sprite.setRotation((float)(Math.atan2(helper2.y - helper.y, helper2.x - helper.x) * 180f/Math.PI) - 90);
			// Shift the sprites around or something
		}

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
		// Magic formula, the only part that doesnt need tweaking is the type of operation
		// This assumes that worldPos.z will never be negative though.
		shadow.setAlpha((float)Math.pow(1 / 2f, 1 / 20f * worldPos.z));
		shadow.draw(batch);
	}
}
