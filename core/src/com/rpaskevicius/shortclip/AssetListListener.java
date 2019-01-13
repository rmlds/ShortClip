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

    private ShortClip currentScreen;

    public AssetListListener(ShortClip currentScreen, List<String> list, NodeActor nodeActor, Table centerUI, ScrollPane scrollPane, NodeGestureListener nodeGestureListener) {
        this.currentScreen = currentScreen;
        this.list = list;
        this.nodeActor = nodeActor;
        this.centerUI = centerUI;
        this.scrollPane = scrollPane;
        this.nodeGestureListener = nodeGestureListener;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        System.out.println("AssetListListener clicked " + list.getSelectedIndex() + " -> " + list.getSelected());

        //TODO send the change to the server
        NetworkMessage message = new NetworkMessage();
        message.build(0, 2, 9);
        message.writeID(nodeActor.getNodeID());
        message.writeCore(new byte[] {(byte) list.getSelectedIndex()}, 8);

        System.out.println("Sending message to update node sound. Core: ");
        System.out.println(message.debugCore(9));

        currentScreen.getDataHandler().writeMessage(message);

        //Handle the change
        String sound = list.getSelected();

        System.out.println(sound);
        nodeActor.setSound(sound);

        centerUI.removeActor(scrollPane);

        nodeGestureListener.setSelectorEnabled(false); //prevent the user from adding the selector multiple times
    }
}
