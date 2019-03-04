package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class NetworkedPan extends ActorGestureListener {

    private NetworkedActor actor;
    protected ShortClip screen;

    public NetworkedPan(NetworkedActor actor, ShortClip screen) {
        this.actor = actor;
        this.screen = screen;
    }

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        offsetPosition(deltaX, deltaY);
        event.handle(); //inform stage that this event has been handled
    }

    public void offsetPosition(float deltaX, float deltaY) {
        if (Math.abs(deltaX) > 0.1f || Math.abs(deltaY) > 0.1f) {
            //prevents spamming the server with packets when
            //the finger is held in place (only needed for Android)

            float newX = actor.getX() + deltaX;
            float newY = actor.getY() + deltaY;

            actor.setPosition(newX, newY);

            deliverPosition(newX, newY);
        }
    }

    public void deliverPosition(float newX, float newY) {
        NetworkMessage message = new NetworkMessage();

        message.build(NetworkMap.getCode(actor), 1, 16);
        message.writeStr(actor.getID());
        message.writeInt((int)newX, 8);
        message.writeInt((int)newY, 12);

        screen.getDataHandler().writeMessage(message);
    }

}
