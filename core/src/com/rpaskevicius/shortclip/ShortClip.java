package com.rpaskevicius.shortclip;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.sound.sampled.Line;

public class ShortClip extends ApplicationAdapter {

	private Stage stageUI;
	private Stage stage;

	private TimeDispatcher time;
	
	@Override
	public void create () {
		stageUI = new Stage(new ExtendViewport(640, 360));
		stage = new Stage(new ExtendViewport(640, 360));

		time = new TimeDispatcher(120.0f);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stageUI);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(new GestureDetector(new CameraGestureListener(stage.getCamera())));
		Gdx.input.setInputProcessor(multiplexer);

		NodeActor node1 = new NodeActor(0, 0, "node-purple-w-connector.png", "kick-01.wav");
		NodeActor node2 = new NodeActor(100, 100, "node-blue.png", "shaker-01.wav");
		NodeActor node3 = new NodeActor(50, 50, "node-blue.png", "closed-hihat-01.wav");

		stage.addActor(node1);
		stage.addActor(node2);
		stage.addActor(node3);

		SequencerActor sequencer1 = new SequencerActor(0, 0, "sequencer-grey-w-panel-white.png", 16, 32, stage);
		SequencerActor sequencer2 = new SequencerActor(200, 200, "sequencer-grey-w-panel-white.png", 16, 32, stage);
		SequencerActor sequencer3 = new SequencerActor(100, 100, "sequencer-grey-w-panel-white.png", 16, 32, stage);

		stage.addActor(sequencer1);
		stage.addActor(sequencer2);
		stage.addActor(sequencer3);

		time.addListener(sequencer1);
		time.addListener(sequencer2);
		time.addListener(sequencer3);

		time.start();

		//-------------------------------------------------

		Skin skin = new Skin();

		skin.add("ui-add-node-texture", new Texture(Gdx.files.internal("ui-add-node.png")));
		skin.add("ui-add-sequencer-texture", new Texture(Gdx.files.internal("ui-add-sequencer.png")));

		ButtonStyle addNodeStyle = new ButtonStyle();
		addNodeStyle.up = skin.newDrawable("ui-add-node-texture", Color.WHITE);
		addNodeStyle.down = skin.newDrawable("ui-add-node-texture", Color.LIGHT_GRAY);
		skin.add("ui-add-node", addNodeStyle);

		ButtonStyle addSequencerStyle = new ButtonStyle();
		addSequencerStyle.up = skin.newDrawable("ui-add-sequencer-texture", Color.WHITE);
		addSequencerStyle.down = skin.newDrawable("ui-add-sequencer-texture", Color.LIGHT_GRAY);
		skin.add("ui-add-sequencer", addSequencerStyle);

		// Create a table that fills the screen
		Table table = new Table();
		table.setFillParent(true);

		Table lowerUI = new Table();

		final Button addNode = new Button(skin, "ui-add-node");
		final Button addSequencer = new Button(skin, "ui-add-sequencer");

		addNode.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("addNode clicked.");
			}
		});

		addSequencer.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("addSequencer clicked.");
			}
		});

		lowerUI.add(addSequencer);
		lowerUI.add(addNode);

		table.add(lowerUI).expand().bottom().right();

		stageUI.addActor(table);
//		table.setDebug(true);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		stage.act(delta);
		stage.draw();

		stageUI.act(delta);
		stageUI.draw();

		time.update();
	}

	@Override
	public void dispose () {
		stage.dispose();
	}
}
