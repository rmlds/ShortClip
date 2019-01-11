package com.rpaskevicius.shortclip;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MainMenuScreen extends ScreenAdapter {

    private Game launchScreen;
    private AssetManager assetManager;

    private Socket socket;
    private NetworkHandler networkHandler;

    private Stage stageUI;
    private Stage stage;

    public MainMenuScreen(Game launchScreen, AssetManager assetManager) {
        this.launchScreen = launchScreen;
        this.assetManager = assetManager;

        System.out.println("Connecting...");
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 13, null);
        System.out.println("Connected successfully.");

        networkHandler = new NetworkHandler(socket);

        stageUI = new Stage(new ExtendViewport(640, 360));
        stage = new Stage(new ExtendViewport(640, 360));

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stageUI);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        //User interface
        Skin skin = createSkin();

        Table table = new Table();
        table.setFillParent(true);

        Table mainUI = createMainUI(skin);
        table.add(mainUI).expand().top().left();

        stageUI.addActor(table);
		stageUI.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        networkHandler.readMessage();

        stage.act(deltaTime);
        stage.draw();

        stageUI.act(deltaTime);
        stageUI.draw();
    }

    @Override
    public void dispose () {
        stageUI.dispose();
        stage.dispose();

        //TODO dispose resources
    }

    private Skin createSkin() {
        Skin skin = new Skin();

        skin.add("default", new BitmapFont());

        skin.add("text-button-texture", new Texture(Gdx.files.internal("list-selected.png")));

        TextButton.TextButtonStyle createRoomStyle = new TextButton.TextButtonStyle();
        createRoomStyle.up = skin.newDrawable("text-button-texture", Color.WHITE);
        createRoomStyle.down = skin.newDrawable("text-button-texture", Color.LIGHT_GRAY);
        createRoomStyle.font = skin.getFont("default");
        skin.add("ui-create-room", createRoomStyle);

        TextButton.TextButtonStyle joinRoomStyle = new TextButton.TextButtonStyle();
        joinRoomStyle.up = skin.newDrawable("text-button-texture", Color.WHITE);
        joinRoomStyle.down = skin.newDrawable("text-button-texture", Color.LIGHT_GRAY);
        joinRoomStyle.font = skin.getFont("default");
        skin.add("ui-join-room", joinRoomStyle);

        return skin;
    }

    private Table createMainUI(Skin skin) {
        Table mainUI = new Table();

        TextButton createRoom = new TextButton("create room", skin, "ui-create-room");
        TextButton joinRoom = new TextButton("join room", skin, "ui-join-room");

        createRoom.addListener(new CreateRoomListener(networkHandler));
        joinRoom.addListener(new JoinRoomListener(networkHandler));

        mainUI.add(createRoom);
        mainUI.row();
        mainUI.add(joinRoom);

        return mainUI;
    }
}
