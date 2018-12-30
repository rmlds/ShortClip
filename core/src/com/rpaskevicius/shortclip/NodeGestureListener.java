package com.rpaskevicius.shortclip;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class NodeGestureListener extends ActorGestureListener {

    private NodeActor nodeActor;

    private AssetManager assetManager;
    private String[] sounds;

    public NodeGestureListener(NodeActor nodeActor, AssetManager assetManager) {
        this.nodeActor = nodeActor;
        this.assetManager = assetManager;

        //sounds should be the same as in AssetLoadingScreen //TODO should be static
        sounds = new String[] {
                "closed-hihat-01.wav",
                "closed-hihat-02.wav",
                "kick-01.wav",
                "kick-02.wav",
                "kick-03.wav",
                "open-hihat-01.wav",
                "shaker-01.wav",
                "shaker-02.wav",
                "shaker-03.wav",
                "snare-01.wav",
                "snare-02.wav",
                "tambourine-01.wav"
        };
    }

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        //Move the node
        nodeActor.setPosition(nodeActor.getX() + deltaX, nodeActor.getY() + deltaY);
        System.out.println("Node: " + nodeActor.getX() + " " + nodeActor.getY());

        //Moving the node also updates lineEnd position
        if (nodeActor.hasSequencer() && nodeActor.getSequencer().hasLine()) {

            Vector2 lineEnd = nodeActor.getConnectionPoint();

            nodeActor.getSequencer().getLine().setEnd(lineEnd);
        }

        event.handle(); //inform Stage that this event has been handled
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        System.out.println("Tap.");

        //TODO open UI to choose a different sound
    }
}
