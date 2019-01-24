package com.rpaskevicius.shortclip;

public class AssetMap {

    public static final String[] sounds = new String[] {
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

    public static final String[] instruments = new String[] {
        "bass",
        "piano"
    };

    public static final String defaultSound = "kick-01.wav";
    public static final String defaultInstrument = "piano";

    public static String getSoundString(int index) {
        return sounds[index];
    }
    public static String getInstrumentString(int index) {
        return instruments[index];
    }

}
