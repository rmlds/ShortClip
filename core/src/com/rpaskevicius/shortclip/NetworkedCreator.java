package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NetworkedCreator extends ClickListener {

    protected ShortClip screen;

    public NetworkedCreator(ShortClip screen) {
        this.screen = screen;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Vector3 center = getCameraPosition();
        deliverCreate(center.x, center.y);
    }

    public Vector3 getCameraPosition() {
        return screen.getStage().getCamera().position;
    }

    public void deliverCreate(float x, float y) {
        NetworkMessage message = new NetworkMessage();

        message.build(NetworkMap.getCode(this), 0, 8);
        message.writeInt((int)x, 0);
        message.writeInt((int)y, 4);

        screen.getDataHandler().writeMessage(message);
    }

}
