package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class BpmButtonListener extends DragListener {

    private ShortClip currentScreen;
    private TextButton button;

    public BpmButtonListener(ShortClip currentScreen, TextButton button) {
        this.currentScreen = currentScreen;
        this.button = button;
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        if (Math.abs(getDeltaX()) < 0.1f) {
            return;
        }

        float bpm = currentScreen.getTimeDispatcher().getBpm() - getDeltaX();

        if (bpm < 40.0f) {
            bpm = 40.0f;
        } else if (bpm > 240.0f) {
            bpm = 240.0f;
        }

        currentScreen.getTimeDispatcher().setBpm(bpm);

        button.setText((int)currentScreen.getTimeDispatcher().getBpm() + "");

        //send bpm to server
        NetworkMessage message = new NetworkMessage();
        message.build(11, 0, 4);

        message.writeInt((int)bpm, 0);

        currentScreen.getDataHandler().writeMessage(message);
    }
}
