package com.rpaskevicius.shortclip;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;

public class MainInputHandler extends InputAdapter {
    private Sound snd;

    public MainInputHandler (Sound snd) {
        this.snd = snd;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        // your touch down code here
        snd.play(1.0f);
        return true; // return true to indicate the event was handled
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        // your touch up code here
        return true; // return true to indicate the event was handled
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {

        return true;
    }
}
