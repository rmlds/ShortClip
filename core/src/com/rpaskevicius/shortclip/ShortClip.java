package com.rpaskevicius.shortclip;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.sound.sampled.Line;

public class ShortClip extends ApplicationAdapter {

	private Stage stage;
	private TimeDispatcher time;
	
	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		time = new TimeDispatcher(120.0f);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(new GestureDetector(new CameraGestureListener(stage.getCamera())));
		Gdx.input.setInputProcessor(multiplexer);

		NodeActor node1 = new NodeActor(0, 0, "node-purple.png", "kick-1.wav");
		NodeActor node2 = new NodeActor(100, 100, "node-blue.png", "clap-1.wav");

		stage.addActor(node1);
		stage.addActor(node2);

		SequencerActor sequencer1 = new SequencerActor(0, 0, "sequencer-grey.png", 16, stage);
		SequencerActor sequencer2 = new SequencerActor(200, 200, "sequencer-grey.png", 16, stage);

		sequencer1.setNode(node1);
        sequencer2.setNode(node2);

		stage.addActor(sequencer1);
		stage.addActor(sequencer2);

//		time.addListener(sequencer1);
//		time.addListener(sequencer2);

		time.start();

//		LineActor line1 = new LineActor(new Vector2(10,10), new Vector2(210,110), "line-segment.png");
//		stage.addActor(line1);


	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		stage.act(delta);
		stage.draw();

		time.update();
	}

	@Override
	public void dispose () {
		stage.dispose();
	}
}
