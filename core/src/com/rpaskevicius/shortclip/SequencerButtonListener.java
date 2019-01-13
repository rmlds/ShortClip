package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SequencerButtonListener extends ClickListener {
    private ShortClip currentScreen;
    private Stage stage;
    private TimeDispatcher time;

    public SequencerButtonListener(ShortClip currentScreen, Stage stage, TimeDispatcher time) {
        this.currentScreen = currentScreen;
        this.stage = stage;
        this.time = time;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Vector3 center = stage.getCamera().position;

        //ask server to create the sequencer
        NetworkMessage message = new NetworkMessage();
        message.build(1, 0, 8); // don't forget to include coreLength!
        message.writeInt((int)center.x, 0); //we also need to send coords in message core
        message.writeInt((int)center.y, 4);

        System.out.println("Sending message to create new sequencer. Debug: ");
        System.out.println(message.debug(8));

        currentScreen.getDataHandler().writeMessage(message);

        /*
        SequencerActor sequencer = new SequencerActor(center.x, center.y, "sequencer-grey-w-panel-white.png", 16, 32, stage);

        time.addListener(sequencer);

        stage.addActor(sequencer);

        System.out.println("Sequencer actor created: " + center.x + " " + center.y);
        */
    }
}
