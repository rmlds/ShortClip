package com.rpaskevicius.shortclip;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.utils.Align;
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

	private ConnectionVisualizer connectionVisualizer;

	private Button playButton;
    private TextButton bpmButton;

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

		//time.start(); //time is controlled by the server

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
		//stageUI.setDebugAll(true);

		//Sequencer and node connection visualizer
		this.connectionVisualizer = new ConnectionVisualizer(this);

		//--------------------------------------

		PianoRollActor p = new PianoRollActor("ABCDEFGH", 0, 0, this);
		time.addListener(p);
		stage.addActor(p);

		InstrumentActor in = new InstrumentActor("XYZABCDE", 550, 100, this);
		stage.addActor(in);
		p.setInstrument(in);
	}

	@Override
	public void render(float delta) {
		float deltaTime = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		dataHandler.readMessage();

		stage.act(deltaTime);
		stage.draw();

		connectionVisualizer.drawConnections(stage.getBatch());

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

		skin.add("ui-play-texture", new Texture(Gdx.files.internal("ui-play.png")));
		skin.add("ui-stop-texture", new Texture(Gdx.files.internal("ui-stop.png")));
        skin.add("ui-bpm-texture", new Texture(Gdx.files.internal("ui-bpm.png")));

		ButtonStyle playStyle = new ButtonStyle();
		playStyle.up = skin.newDrawable("ui-play-texture", Color.WHITE);
		playStyle.down = skin.newDrawable("ui-play-texture", Color.LIGHT_GRAY);
		playStyle.checked = skin.newDrawable("ui-stop-texture", Color.WHITE);
		skin.add("ui-play", playStyle);

        TextButton.TextButtonStyle bpmStyle = new TextButton.TextButtonStyle();
        bpmStyle.up = skin.newDrawable("ui-bpm-texture", Color.WHITE);
        bpmStyle.down = skin.newDrawable("ui-bpm-texture", Color.LIGHT_GRAY);
        bpmStyle.font = skin.getFont("default");
        skin.add("ui-bpm", bpmStyle);

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

		addNode.addListener(new NodeCreator(this));
		addSequencer.addListener(new SequencerCreator(this));

		lowerUI.add(addSequencer);
		lowerUI.add(addNode);

		return lowerUI;
	}

	private Table createUpperUI(Skin skin) {
		Table upperUI = new Table();

		playButton = new Button(skin, "ui-play");
		bpmButton = new TextButton((int)time.getBpm() + "", skin, "ui-bpm");
		Label roomLabel = new Label("Room ID: " + roomID, skin, "ui-room-label");

		playButton.addListener(new PlayButtonListener(this, playButton));
        bpmButton.addListener(new BpmButtonListener(this, bpmButton));

		upperUI.add(playButton);
		upperUI.add(bpmButton);
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

	public NodeActor getNodeByID(String ID) {
		for (Actor actor : stage.getActors()) {
			if (actor instanceof NodeActor) {
				if (((NodeActor) actor).getID().equals(ID)) {
					return ((NodeActor) actor);
				}
			}
		}

		return null;
	}

	public SequencerActor getSequencerByID(String ID) {
		for (Actor actor : stage.getActors()) {
			if (actor instanceof SequencerActor) {
				if (((SequencerActor) actor).getID().equals(ID)) {
					return ((SequencerActor) actor);
				}
			}
		}

		return null;
	}

	public Button getPlayButton() {
		return this.playButton;
	}

    public TextButton getBpmButton() {
        return bpmButton;
    }

}
