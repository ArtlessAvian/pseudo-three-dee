package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class TestTestTestTestTestTest extends ApplicationAdapter
{
	float camTargX = 0;
	float camTargY = 0;

	final float toRadians = (float)Math.PI/180f;

	float camAngle = 30 * toRadians;
	float camDistance = 300;

	PerspectiveCamera camera;
	OrthographicCamera identity; // Never touch identity. It screws everything up

	SpriteBatch batch;
	BitmapFont font;
	Texture luukass;
	Texture tile;

	SpriteWrapperThingy spriteWrapperThingy;
	
	@Override
	public void create()
	{
		batch = new SpriteBatch();
		font = new BitmapFont();

		camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		identity = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		luukass = new Texture("Test.png");
		tile = new Texture("Tile.png");

		spriteWrapperThingy = new SpriteWrapperThingy();

		for (int i = 0; i < 900; i++)
		{
			spriteWrapperThingy.add(new SpriteWrapper(luukass, new Vector3(16*(int)((Math.random()*1200-600)/16), 16*(int)((Math.random()*1200-600)/16), 0)));
		}

//		for (int y = -300; y <= 300; y += 32)
//		{
//			for (int x = -300; x <= 300; x += 32)
//			{
//				spriteWrapperThingy.add(new SpriteWrapper(luukass, new Vector3(x, y, 0)));
//			}
//		}
	}

	@Override
	public void resize(int width, int height)
	{
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.position.x = camTargX;
		camera.position.y = camTargY + camDistance * -(float)Math.cos(camAngle);
		camera.position.z = 0 + camDistance * (float)Math.sin(camAngle);
		camera.lookAt(camTargX, camTargY, 0);

		camera.near = 0;
		camera.far = 300000;
		camera.update();

		identity.viewportWidth = width;
		identity.viewportHeight = height;
		identity.position.x = width / 2f;
		identity.position.y = height / 2f;
		identity.update();
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camTargY += 1;}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camTargX -= 1;}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camTargY -= 1;}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camTargX += 1;}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {camAngle = Math.min(90 * toRadians, camAngle + 0.01f);}
		if (Gdx.input.isKeyPressed(Input.Keys.F)) {camAngle = Math.max(0.00001f, camAngle - 0.01f);}
		if (Gdx.input.isKeyPressed(Input.Keys.T)) {camDistance = Math.max(0.001f, camDistance - 2);}
		if (Gdx.input.isKeyPressed(Input.Keys.G)) {camDistance = Math.min(300, camDistance + 2);}

		camera.position.x = camTargX;
		camera.position.y = camTargY + camDistance * -(float)Math.cos(camAngle);
		camera.position.z = 0 + camDistance * (float)Math.sin(camAngle);
		camera.lookAt(camTargX, camTargY, 0);

		camera.update();

		batch.begin();

		batch.setProjectionMatrix(camera.combined);
		for (int y = 600; y >= -600; y -= 16)
		{
			for (int x = 600; x >= -600; x -= 16)
			{
				batch.draw(tile, x, y, 16, 16);
			}
		}

		batch.setProjectionMatrix(identity.combined);
		spriteWrapperThingy.draw(batch, camera);

		font.draw(batch, Gdx.graphics.getFramesPerSecond() + "", 35, identity.viewportHeight - 35);

		batch.end();
	}

	@Override
	public void dispose ()
	{
		tile.dispose();
		luukass.dispose();
		batch.dispose();
		font.dispose();
	}
}
