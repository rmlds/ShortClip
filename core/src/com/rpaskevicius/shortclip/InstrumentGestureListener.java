package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class InstrumentGestureListener extends NetworkedPan {

    private InstrumentActor actor;
    private InstrumentAssetSelector selector;

    public InstrumentGestureListener(InstrumentActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;

        selector = new InstrumentAssetSelector(actor, screen);
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        selector.enable();
    }

    public InstrumentAssetSelector getAssetSelector() { return selector; }

}
