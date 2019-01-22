package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class SequencerGestureListener extends NetworkedLinker {

    private SequencerActor actor;

    public SequencerGestureListener(SequencerActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (x < actor.getEffectiveArea()) {
            float stepWidth = actor.getEffectiveArea() / actor.getStepCount();

            int stepIndex = (int) (x / stepWidth);

            if (actor.getStepState(stepIndex)) {
                actor.setStepState(stepIndex, false);
            } else {
                actor.setStepState(stepIndex, true);
            }

            //send steps to server
            NetworkMessage message = new NetworkMessage();
            message.build(1, 2, 24);
            message.writeStr(actor.getID());
            message.writeBoolArr(actor.getSteps(), 8);

            screen.getDataHandler().writeMessage(message);
        }
    }

    @Override
    public void linkHandler(Actor hitResult) {
        if (hitResult instanceof NodeActor) {
            NodeActor node = (NodeActor) hitResult;

            actor.clearNode();
            actor.setNode(node);

            deliverReference(node);

        } else {
            actor.clearNode();

            deliverClear();
        }
    }

}
