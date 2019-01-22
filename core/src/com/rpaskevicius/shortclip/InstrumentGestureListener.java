package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class InstrumentGestureListener extends NetworkedPan {

    private InstrumentActor actor;

    public InstrumentGestureListener(InstrumentActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        System.out.println("Tap");
        //TODO open UI to change the instrument
    }

}
