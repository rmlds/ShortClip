package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class SequencerGestureListener extends ActorGestureListener {

    private SequencerActor sequencerActor;
    private Stage stage;

    LineActor line;

    public SequencerGestureListener(SequencerActor sequencerActor, Stage stage) {
        this.sequencerActor = sequencerActor;
        this.stage = stage;

//        Vector2 position = new Vector2(sequencerActor.getX(), sequencerActor.getY());
//        line = new LineActor(position, position, "line-segment.png");

//        this.stage.addActor(line);
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        float stepWidth = sequencerActor.getWidth() / sequencerActor.getStepCount();

        int stepIndex = (int) (x / stepWidth);

        if (sequencerActor.getStepState(stepIndex)) {
            sequencerActor.setStepState(stepIndex, false);
        } else {
            sequencerActor.setStepState(stepIndex, true);
        }

        System.out.println("Tap: " + x + " " + y + " " + stepIndex);
    }

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        sequencerActor.setPosition(sequencerActor.getX() + deltaX, sequencerActor.getY() + deltaY);

        Vector2 stageCoords = sequencerActor.localToStageCoordinates(new Vector2(x, y));
        System.out.println("Node: " + stageCoords.x + " " + stageCoords.y);

//        Vector2 oldEnd = line.getEnd();
//        line.setEnd(new Vector2(oldEnd.x + deltaX, oldEnd.y + deltaY));


//        System.out.println("Sequencer: " + sequencerActor.getX() + " " + sequencerActor.getY());

        event.handle(); //inform Stage that this event has been handled
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Sequencer: " + x + " " + y);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

    }
}
