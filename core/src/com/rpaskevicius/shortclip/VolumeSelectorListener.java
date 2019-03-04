package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class VolumeSelectorListener extends DragListener {

    private VolumeActor actor;
    private ShortClip screen;

    public VolumeSelectorListener(VolumeActor actor, ShortClip screen) {
        this.actor = actor;
        this.screen = screen;
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        int deltaY = (int) getDeltaY();

        if (deltaY == 0) {
            return;
        }

        int volume = actor.getVolume() - deltaY;

        if (volume < 0) {
            volume = 0;
        } else if (volume > 100) {
            volume = 100;
        }

        actor.setVolume(volume);
        actor.getVolumeSelector().setText(Integer.toString(volume));

        deliverVolume(volume);
    }

    public void deliverVolume(int volume) {
        NetworkMessage message = new NetworkMessage();

        message.build(NetworkMap.getCode(actor), 3, 9);
        message.writeStr(actor.getID());
        message.writeByte(volume, 8);

        screen.getDataHandler().writeMessage(message);
    }

}
