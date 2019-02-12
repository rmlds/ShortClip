package com.rpaskevicius.shortclip;

public class NetworkMap {

    public static int getCode(NetworkedActor actor) {
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

    public static int getCode(NetworkedCreator creator) {
        if (creator instanceof NodeCreator) {
            return 0;
        } else if (creator instanceof SequencerCreator) {
            return 1;
        } else if (creator instanceof InstrumentCreator) {
            return 2;
        } else if (creator instanceof PianoRollCreator) {
            return 3;
        } else {
            return 44; //not found
        }
    }

    public static int getCode(AssetSelector selector) {
        if (selector instanceof NodeAssetSelector) {
            return 0;
        } else if (selector instanceof InstrumentAssetSelector) {
            return 2;
        } else if (selector instanceof ScaleSelector) { //PianoRoll
            return 3;
        } else {
            return 44; //not found
        }
    }

    public static int getCode(VolumeActor actor) {
        if (actor instanceof NodeActor) {
            return 0;
        } else if (actor instanceof InstrumentActor) {
            return 2;
        } else {
            return 44; //not found
        }
    }

}
