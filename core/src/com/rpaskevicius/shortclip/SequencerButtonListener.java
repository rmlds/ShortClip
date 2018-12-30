package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SequencerButtonListener extends ClickListener {
    private Stage stage;
    private TimeDispatcher time;

    public SequencerButtonListener(Stage stage, TimeDispatcher time) {
        this.stage = stage;
        this.time = time;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Vector3 center = stage.getCamera().position;

        SequencerActor sequencer = new SequencerActor(center.x, center.y, "sequencer-grey-w-panel-white.png", 16, 32, stage);

        time.addListener(sequencer);

        stage.addActor(sequencer);

        System.out.println("Sequencer actor created: " + center.x + " " + center.y);
    }
}
