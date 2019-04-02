package com.rpaskevicius.shortclip;

public class AssetMap {

    public static final String[] sounds = new String[] {
        "closed-hihat-01.wav",
        "closed-hihat-02.wav",
        "kick-01.wav",
        "clap-01.wav"
    };

    public static final String[] instruments = new String[] {
        "warm-bass",
        "lofi-chords",
        "warm-pad",
        "soft-stab"
    };

    public static final String defaultSound = "kick-01.wav";
    public static final String defaultInstrument = "lofi-chords";

    public static String getSoundString(int index) {
        return sounds[index];
    }
    public static String getInstrumentString(int index) {
        return instruments[index];
    }

}
