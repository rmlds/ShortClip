package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class NodeGestureListener extends ActorGestureListener {

    private NodeActor nodeActor;

    public NodeGestureListener(NodeActor nodeActor) {
        this.nodeActor = nodeActor;
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
}
