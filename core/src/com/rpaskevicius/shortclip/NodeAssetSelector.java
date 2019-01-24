package com.rpaskevicius.shortclip;

public class NodeAssetSelector extends AssetSelector {

    private NodeActor actor;

    public NodeAssetSelector(NodeActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;

        setup(AssetMap.sounds, AssetMap.defaultSound);
    }

    @Override
    public void updateHost(String selection) {
        actor.setSound(selection);
    }

}
