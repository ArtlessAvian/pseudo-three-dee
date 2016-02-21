package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class TestTestTestTestTestTest extends ApplicationAdapter
{
	PerspectiveCamera linMapping;
	OrthographicCamera camera;

	SpriteBatch batch;
	BitmapFont font;
	Texture luukass;
	Texture tile;

	Vector3 unproj;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		linMapping = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		linMapping.position.x = 0;
		linMapping.position.y = -600;
		linMapping.position.z = 200;
		linMapping.lookAt(0,0,0);

		linMapping.far = 300000;
		linMapping.update();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.x = camera.viewportWidth/2f;
		camera.position.y = camera.viewportHeight/2f;
		camera.update();

		luukass = new Texture("Test.png");
		tile = new Texture("Tile.png");

		unproj = new Vector3(0,0,0);
	}

	@Override
	public void resize(int width, int height)
	{
		linMapping.viewportWidth = width;
		linMapping.viewportHeight = height;
		linMapping.position.x = 0;
		linMapping.position.y = -600;
		linMapping.position.z = 200;
		linMapping.lookAt(0,0,0);

		linMapping.far = 300000;
		linMapping.update();

		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.position.x = camera.viewportWidth/2f;
		camera.position.y = camera.viewportHeight/2f;
		camera.update();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		batch.setProjectionMatrix(linMapping.combined);
		for (int y = 300; y >= -300; y -= 100)
		{
			for (int x = 300; x >= -300; x -= 100)
			{
				batch.draw(tile, x, y, 100, 100);
				font.draw(batch, x + " " + y, x, y + 30);
			}
		}

		batch.setProjectionMatrix(camera.combined);
		// FPS is bad, but theres only like 900 rectangles going up and down or so
		for (int y = 300; y >= -300; y -= 10)
		{
			for (int x = 300; x >= -300; x -= 10)
			{
				unproj.x = x;
				unproj.y = y;
				// omg try something dumb like tan, or any other cyclic function (csc)?
				unproj.z = 30 + 30 * (float)Math.sin(x/50f + y/75f + Gdx.graphics.getFrameId()/30f);
				linMapping.project(unproj);
				batch.draw(luukass, unproj.x, unproj.y);
			}
		}

		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " ", 30, 30);

		batch.end();
	}

	@Override
	public void dispose()
	{
		tile.dispose();
		luukass.dispose();
		batch.dispose();
		font.dispose();
	}
}
