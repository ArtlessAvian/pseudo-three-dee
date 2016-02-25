package com.artlessavian.pseudothreedee;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;

public class TestTestTestTestTestTest extends ApplicationAdapter
{
	PerspectiveCamera camera;
	OrthographicCamera oneToOne; // There should be no good reason to modify this.
	FirstPersonCameraController fpcc;

	SpriteBatch batch;
	BitmapFont font;
	Texture shadow;
	Texture luukass;
	Texture tile;

	BillboardManager billboards;
	
	@Override
	public void create()
	{
		batch = new SpriteBatch();
		font = new BitmapFont();

		camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fpcc = new FirstPersonCameraController(camera);
		fpcc.setVelocity(100);
		Gdx.input.setInputProcessor(fpcc);

		camera.position.x = 0;
		camera.position.y = -320;
		camera.position.z = 100;
		camera.lookAt(0, -319, 100);

		camera.near = 1;
		camera.far = 3000;
		camera.update();

		oneToOne = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		shadow = new Texture("Shadow.png");
		luukass = new Texture("Test.png");
		tile = new Texture("Tile.png");

		billboards = new BillboardManager(camera, oneToOne);

		for (int i = 0; i < 1200; i++)
		{
			billboards.add(new BillboardSprite(luukass, shadow, 16f, new Vector3((float)(Math.random() * 640 - 320), (float)(Math.random() * 640 - 320), 0)));
		}
	}

	@Override
	public void resize(int width, int height)
	{
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();

		oneToOne.viewportWidth = width;
		oneToOne.viewportHeight = height;
		oneToOne.position.x = width / 2f;
		oneToOne.position.y = height / 2f;
		oneToOne.update();
	}

	@Override
	public void render()
	{
		fpcc.update(Gdx.graphics.getDeltaTime());

		// Update the positions
		for (BillboardSprite b : billboards.billboards)
		{
			b.worldPos.z = Math.max(0, 40f * (float)Math.sin(b.worldPos.x / 75f + b.worldPos.y / 150f + Gdx.graphics.getFrameId() / 60f));
		}

		// Rendering
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		// Render Ground Stuff
		batch.setProjectionMatrix(camera.combined);
		for (int y = 320; y >= -320; y -= 16)
		{
			for (int x = 320; x >= -320; x -= 16)
			{
				batch.draw(tile, x, y, 16, 16);
			}
		}

		// Render Billboarded Sprites + Shadows
		billboards.draw(batch);

		font.draw(batch, Gdx.graphics.getFramesPerSecond() + "", 35, oneToOne.viewportHeight - 35);

		batch.end();
	}

	@Override
	public void dispose()
	{
		tile.dispose();
		shadow.dispose();
		luukass.dispose();
		batch.dispose();
		font.dispose();
	}
}
