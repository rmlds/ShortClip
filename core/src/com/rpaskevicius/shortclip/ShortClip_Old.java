package com.rpaskevicius.shortclip;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import sun.security.ssl.Debug;

public class ShortClip_Old extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	public Sound mySound;
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;
	Sprite node;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		// img = new Texture("node-purple.png");
		mySound = Gdx.audio.newSound(Gdx.files.internal("kick-1.wav"));
		shapeRenderer = new ShapeRenderer();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// Constructs a new OrthographicCamera, using the given viewport width and height
		camera = new OrthographicCamera(w, h);

		//camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		// camera.update();

		node = new Sprite(new Texture(Gdx.files.internal("node-purple.png")));
		node.setPosition(0, 0);
		node.setSize(128, 128);

		//MainInputHandler inputHandler = new MainInputHandler(this.mySound);
		//Gdx.input.setInputProcessor(inputHandler);

		Gdx.input.setInputProcessor(new GestureDetector(new MainGestureHandler(camera)));
	}

	@Override
	public void render () {
		camera.update();

		Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/*
		batch.begin();
		batch.draw(img, 100, 0);
		batch.end();
		*/

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		node.draw(batch);
		batch.end();

        boolean justTouched = Gdx.input.justTouched();
        if (justTouched) {
            //mySound.play(1.0f);

			//camera.translate(1, 0, 0);
        }

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
