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
        float deltaX = -getDeltaX();

        if (Math.abs(deltaX) < 0.1f) {
            return;
        }

        float currentBPM = currentScreen.getTimeDispatcher().getBpm();

        if (currentBPM < 40.1f && deltaX < 0) {
            return;
        } else if (currentBPM > 239.9f && deltaX > 0) {
            return;
        }

        float newBPM = currentBPM + deltaX;

        if (newBPM < 40.0f) {
            newBPM = 40.0f;
        } else if (newBPM > 240.0f) {
            newBPM = 240.0f;
        }

        currentScreen.getTimeDispatcher().setBpm(newBPM);

        button.setText((int)currentScreen.getTimeDispatcher().getBpm() + "");

        //send bpm to server
        NetworkMessage message = new NetworkMessage();
        message.build(11, 0, 4);

        message.writeInt((int)newBPM, 0);

        currentScreen.getDataHandler().writeMessage(message);
    }

}
