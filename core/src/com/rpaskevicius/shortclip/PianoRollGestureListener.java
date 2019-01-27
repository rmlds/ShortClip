package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class PianoRollGestureListener extends NetworkedLinker {

    private PianoRollActor actor;

    public PianoRollGestureListener(PianoRollActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (x < actor.getEffectiveArea()) {
            float stepWidth = actor.getEffectiveArea() / actor.getStepCount();
            float stepHeight = 32.0f;

            int stepIndex = (int) (x / stepWidth);
            int toneIndex = (int) (y / stepHeight);

            if (actor.getStepState(toneIndex, stepIndex)) {
                actor.setStepState(toneIndex, stepIndex, false);
            } else {
                actor.setStepState(toneIndex, stepIndex, true);
            }

            //send steps to the server
            NetworkMessage message = new NetworkMessage();

            int param = 3;
            if (actor.getStepState(toneIndex, stepIndex)) { param = 2; }

            message.build(NetworkMap.getCode(actor), param, 9);

            message.writeStr(actor.getID());
            message.writeByte(toneIndex * actor.getStepCount() + stepIndex, 8);

            screen.getDataHandler().writeMessage(message);
        }
    }

    @Override
    public void linkHandler(Actor hitResult) {
        if (hitResult instanceof InstrumentActor) {
            InstrumentActor instrument = (InstrumentActor) hitResult;

            actor.clearInstrument();
            actor.setInstrument(instrument);

            deliverReference(instrument, 4);

        } else {
            actor.clearInstrument();

            deliverClear(5);
        }
    }

}
