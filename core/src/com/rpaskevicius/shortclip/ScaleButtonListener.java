package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ScaleButtonListener extends ClickListener {

    private PianoRollActor actor;
    private ShortClip screen;

    private ScaleSelector selector;

    public ScaleButtonListener(PianoRollActor actor, ShortClip screen) {
        this.actor = actor;
        this.screen = screen;

        selector = new ScaleSelector(actor, screen);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        selector.enable();
    }

    public ScaleSelector getScaleSelector() { return selector; }

}
