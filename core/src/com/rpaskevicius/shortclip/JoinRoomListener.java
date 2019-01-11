package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class JoinRoomListener extends ClickListener {

    private NetworkHandler networkHandler;

    public JoinRoomListener(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        System.out.println("Join room clicked. Sending message...");

        NetworkMessage message = new NetworkMessage();
        message.build(0, 1, 8);
        message.writeCore(new byte[] {72, 85, 77, 69, 65, 89, 76, 78}); //HUMEAYLN

        networkHandler.writeMessage(message);
    }
}
