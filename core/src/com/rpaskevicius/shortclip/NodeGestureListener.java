package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class NodeGestureListener extends NetworkedPan {

    private NodeActor actor;
    private NodeAssetSelector selector;

    public NodeGestureListener(NodeActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;

        selector = new NodeAssetSelector(actor, screen);
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        selector.enable();
    }

    public NodeAssetSelector getAssetSelector() { return selector; }

}
