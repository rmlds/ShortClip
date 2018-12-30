package com.rpaskevicius.shortclip;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NodeButtonListener extends ClickListener {
    private Stage stage;
    private AssetManager assetManager;
    private Table centerUI;

    public NodeButtonListener(Stage stage, AssetManager assetManager, Table centerUI) {
        this.stage = stage;
        this.assetManager = assetManager;
        this.centerUI = centerUI;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Vector3 center = stage.getCamera().position;

        NodeActor node = new NodeActor(center.x, center.y, "node-purple-w-connector.png", "kick-01.wav", assetManager, centerUI);

        stage.addActor(node);

        System.out.println("Node actor created: " + center.x + " " + center.y);
    }
}
