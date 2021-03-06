package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PlayButtonListener extends ClickListener {

    private ShortClip currentScreen;
    private Button button;

    public PlayButtonListener(ShortClip currentScreen, Button button) {
        this.currentScreen = currentScreen;
        this.button = button;

        button.setChecked(false); //game starts with audio stopped
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        /*
        if (button.isChecked()) { //on play
            System.out.println("Starting TimeDispatcher...");
            currentScreen.getTimeDispatcher().start();
        } else { //on stop
            System.out.println("Stopping TimeDispatcher...");
            currentScreen.getTimeDispatcher().stop();
        }
        */

        NetworkMessage message = new NetworkMessage();
        message.build(10, 0);

        currentScreen.getDataHandler().writeMessage(message);
    }
}
