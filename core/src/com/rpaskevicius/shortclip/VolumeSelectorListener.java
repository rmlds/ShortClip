package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class VolumeSelectorListener extends DragListener {

    private NodeActor actor;
    private ShortClip screen;

    public VolumeSelectorListener(NodeActor actor, ShortClip screen) {
        this.actor = actor;
        this.screen = screen;
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        float volume = actor.getVolume() - getDeltaY() / 100.0f;

        if (volume < 0.0f) {
            volume = 0.0f;
        } else if (volume > 1.0f) {
            volume = 1.0f;
        }

        actor.setVolume(volume);
        actor.getVolumeSelector().setText(Integer.toString((int)(volume * 100.0f)));

        deliverVolume(volume);
    }

    public void deliverVolume(float volume) {
        NetworkMessage message = new NetworkMessage();

        message.build(NetworkMap.getCode(actor), 3, 12);
        message.writeStr(actor.getID());
        message.writeInt((int)(volume * 100.0f), 8);

        screen.getDataHandler().writeMessage(message);
    }
}
