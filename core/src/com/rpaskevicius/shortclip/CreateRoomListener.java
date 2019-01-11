package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CreateRoomListener extends ClickListener {

    private NetworkHandler networkHandler;

    public CreateRoomListener(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        System.out.println("Create room clicked. Sending message...");

        NetworkMessage message = new NetworkMessage();
        message.build(0, 0);

        networkHandler.writeMessage(message);
    }
}
