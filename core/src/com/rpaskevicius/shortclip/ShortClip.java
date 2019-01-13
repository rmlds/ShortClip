package com.rpaskevicius.shortclip;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ShortClip extends ScreenAdapter {

	private Stage stageUI;
	private Stage stage;

	private TimeDispatcher time;

	private AssetManager assetManager;

	private Table centerUI;

	private Socket socket;
	private String roomID;

	private DataHandler dataHandler;

	public ShortClip(Game launchScreen, AssetManager assetManager, Socket socket, String roomID) {
		this.assetManager = assetManager;
		this.socket = socket;
		this.roomID = roomID;

		this.dataHandler = new DataHandler(socket, this);

		stageUI = new Stage(new ExtendViewport(640, 360));
		stage = new Stage(new ExtendViewport(640, 360));

		time = new TimeDispatcher(120.0f);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stageUI);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(new GestureDetector(new CameraGestureListener(stage.getCamera())));
		Gdx.input.setInputProcessor(multiplexer);

		time.start();

		//User interface
		Skin skin = createSkin();

		Table table = new Table();
		table.setFillParent(true);

		Table upperUI = createUpperUI(skin);
		table.add(upperUI).expand().top().left();
		table.row();

		centerUI = new Table();
		table.add(centerUI).expand();
		table.row();

		Table lowerUI = createLowerUI(skin);
		table.add(lowerUI).expand().bottom().right();

		stageUI.addActor(table);
		stageUI.setDebugAll(true);
	}

	@Override
	public void render(float delta) {
		float deltaTime = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		dataHandler.readMessage();

		stage.act(deltaTime);
		stage.draw();

		stageUI.act(deltaTime);
		stageUI.draw();

		time.update();
	}

	@Override
	public void dispose () {
		stageUI.dispose();
		stage.dispose();

		//TODO dispose resources
	}

	private Skin createSkin() {
		Skin skin = new Skin();

		//UpperUI
		skin.add("default", new BitmapFont());

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = skin.getFont("default");
		labelStyle.fontColor = Color.WHITE;
		skin.add("ui-room-label", labelStyle);

		//LowerUI
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

		return skin;
	}

	private Table createLowerUI(Skin skin) {
		Table lowerUI = new Table();

		Button addNode = new Button(skin, "ui-add-node");
		Button addSequencer = new Button(skin, "ui-add-sequencer");

		addNode.addListener(new NodeButtonListener(this, stage, assetManager, centerUI));
		addSequencer.addListener(new SequencerButtonListener(this, stage, time));

		lowerUI.add(addSequencer);
		lowerUI.add(addNode);

		return lowerUI;
	}

	private Table createUpperUI(Skin skin) {
		Table upperUI = new Table();

		Label roomLabel = new Label("Room ID: " + roomID, skin, "ui-room-label");

		upperUI.add(roomLabel);

		return upperUI;
	}

	public DataHandler getDataHandler() {
		return this.dataHandler;
	}

	public AssetManager getAssetManager() {
		return this.assetManager;
	}

	public Stage getStage() {
		return this.stage;
	}

	public Table getCenterUI() {
		return centerUI;
	}

	public TimeDispatcher getTimeDispatcher() {
		return time;
	}

}
