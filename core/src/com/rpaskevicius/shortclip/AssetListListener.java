package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AssetListListener extends ClickListener {

    private List<String> list;
    private NodeActor nodeActor;
    private Table centerUI;
    private ScrollPane scrollPane;
    private NodeGestureListener nodeGestureListener;

    public AssetListListener(List<String> list, NodeActor nodeActor, Table centerUI, ScrollPane scrollPane, NodeGestureListener nodeGestureListener) {
        this.list = list;
        this.nodeActor = nodeActor;
        this.centerUI = centerUI;
        this.scrollPane = scrollPane;
        this.nodeGestureListener = nodeGestureListener;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        String sound = list.getSelected();

        System.out.println(sound);
        nodeActor.setSound(sound);

        centerUI.removeActor(scrollPane);

        nodeGestureListener.setSelectorEnabled(false);
    }
}
