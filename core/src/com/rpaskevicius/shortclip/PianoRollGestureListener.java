package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class PianoRollGestureListener extends ActorGestureListener {

    private ShortClip currentScreen;

    private PianoRollActor pianoRollActor;

    public PianoRollGestureListener(ShortClip currentScreen, PianoRollActor pianoRollActor) {
        this.currentScreen = currentScreen;
        this.pianoRollActor = pianoRollActor;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (x < pianoRollActor.getEffectiveArea()) {
            float stepWidth = pianoRollActor.getEffectiveArea() / pianoRollActor.getStepCount();
            float stepHeight = 32.0f;

            int stepIndex = (int) (x / stepWidth);
            int toneIndex = (int) (y / stepHeight);

            System.out.println("Tap toneIndex: " + toneIndex + " stepIndex: " + stepIndex);

            if (pianoRollActor.getStepState(toneIndex, stepIndex)) {
                pianoRollActor.setStepState(toneIndex, stepIndex, false);
            } else {
                pianoRollActor.setStepState(toneIndex, stepIndex, true);
            }
        }
    }

}
