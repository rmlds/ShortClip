package com.rpaskevicius.shortclip;

public class ScaleSelector extends AssetSelector {

    private PianoRollActor actor;

    public ScaleSelector(PianoRollActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;

        setup(ScaleMap.scales, ScaleMap.defaultScale);
    }

    @Override
    public void updateHost(String selection) {
        actor.setScaleMap(ScaleMap.genScaleMap(selection));
        actor.getScaleButton().setText(selection);
    }

    @Override
    public void deliverSelection() {
        //TODO deliver scale to the server
    }

}
