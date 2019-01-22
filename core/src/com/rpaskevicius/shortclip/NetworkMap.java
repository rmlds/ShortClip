package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class NetworkMap {

    public static int getCode(Actor actor) { //TODO change to NetworkedActor
        if (actor instanceof NodeActor) {
            return 0;
        } else if (actor instanceof SequencerActor) {
            return 1;
        } else if (actor instanceof InstrumentActor) {
            return 2;
        } else if (actor instanceof PianoRollActor) {
            return 3;
        } else {
            return 44; //not found
        }
    }

}
