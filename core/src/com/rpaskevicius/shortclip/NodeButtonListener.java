package com.rpaskevicius.shortclip;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NodeButtonListener extends ClickListener {
    private ShortClip currentScreen;
    private Stage stage;
    private AssetManager assetManager;
    private Table centerUI;

    public NodeButtonListener(ShortClip currentScreen, Stage stage, AssetManager assetManager, Table centerUI) {
        this.currentScreen = currentScreen;
        this.stage = stage;
        this.assetManager = assetManager;
        this.centerUI = centerUI;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Vector3 center = stage.getCamera().position;

        //ask server to create the node
        NetworkMessage message = new NetworkMessage();
        message.build(0, 0, 8); // don't forget to include coreLength!
        message.writeCore(SerialUtils.intToArray((int)center.x), 0); //we also need to send coords in message core
        message.writeCore(SerialUtils.intToArray((int)center.y), 4);

        System.out.println("Sending message to create new node. Core: ");
        System.out.println(message.debugCore(8));

        currentScreen.getDataHandler().writeMessage(message);

        /*
        NodeActor node = new NodeActor(center.x, center.y, "node-purple-w-connector.png", "kick-01.wav", assetManager, centerUI);

        stage.addActor(node);

        System.out.println("Node actor created: " + center.x + " " + center.y);
        */
    }
}
