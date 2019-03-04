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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MainMenuScreen extends ScreenAdapter {

    private Game launchScreen;
    private AssetManager assetManager;

    private Socket socket;
    private InitHandler initHandler;

    private Stage stageUI;
    private Stage stage;

    private Label errorLabel;

    public MainMenuScreen(Game launchScreen, AssetManager assetManager) {
        this.launchScreen = launchScreen;
        this.assetManager = assetManager;

        try {
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "10.20.251.149", 80, null);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        /*
        System.out.println("Connecting...");
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "172.22.158.6", 80, null);
        System.out.println("Connected successfully.");
        */

        initHandler = new InitHandler(socket, this);

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
        table.add(mainUI).expand(); //.top().left();

        stageUI.addActor(table);
		//stageUI.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        if (socket != null && socket.isConnected()) {
            initHandler.readMessage();
        }

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

        //Connection UI
        TextButton.TextButtonStyle connectStyle = new TextButton.TextButtonStyle();
        connectStyle.up = skin.newDrawable("text-button-texture", Color.WHITE);
        connectStyle.down = skin.newDrawable("text-button-texture", Color.LIGHT_GRAY);
        connectStyle.font = skin.getFont("default");
        skin.add("ui-connect", connectStyle);

        TextField.TextFieldStyle connectFieldStyle = new TextField.TextFieldStyle();
        connectFieldStyle.background = skin.newDrawable("text-button-texture", Color.LIGHT_GRAY);
        connectFieldStyle.font = skin.getFont("default");
        connectFieldStyle.fontColor = Color.WHITE;
        skin.add("ui-connect-field", connectFieldStyle);
        //Connection UI end

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

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = skin.newDrawable("text-button-texture", Color.LIGHT_GRAY);
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.WHITE;
        skin.add("ui-text-field", textFieldStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        skin.add("ui-label", labelStyle);

        return skin;
    }

    private Table createMainUI(Skin skin) {
        Table mainUI = new Table();

        TextButton connect = new TextButton("connect", skin, "ui-connect");
        TextField connectField = new TextField("172.22.158.6", skin, "ui-connect-field");

        TextButton createRoom = new TextButton("create room", skin, "ui-create-room");
        TextButton joinRoom = new TextButton("join room", skin, "ui-join-room");
        TextField textField = new TextField("HUMEAYLN", skin, "ui-text-field");
        errorLabel = new Label("To join an existing room, enter room ID above", skin, "ui-label");

        createRoom.addListener(new CreateRoomListener(initHandler));
        joinRoom.addListener(new JoinRoomListener(initHandler, textField, errorLabel));
        connect.addListener(new ConnectListener(this, connectField));

        mainUI.add(connect);
        mainUI.row();
        mainUI.add(connectField).padBottom(30f);
        mainUI.row();

        mainUI.add(createRoom);
        mainUI.row();
        mainUI.add(joinRoom);
        mainUI.row();
        mainUI.add(textField);
        mainUI.row();
        mainUI.add(errorLabel);

        return mainUI;
    }

    public Game getLaunchScreen() {
        return launchScreen;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        initHandler.setSocket(socket);
    }
}
