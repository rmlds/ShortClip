package com.rpaskevicius.shortclip;

public class InstrumentAssetSelector extends AssetSelector {

    private InstrumentActor actor;

    public InstrumentAssetSelector(InstrumentActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;

        setup(AssetMap.instruments, AssetMap.defaultInstrument);
    }

    @Override
    public void updateHost(String selection) {
        actor.setInstrument(selection);
    }

}
