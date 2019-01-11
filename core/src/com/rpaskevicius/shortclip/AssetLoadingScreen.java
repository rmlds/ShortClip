package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;

public class AssetLoadingScreen extends ScreenAdapter {

    private Game launchScreen;

    private AssetManager assetManager;

    private String[] sounds;

    public AssetLoadingScreen(Game launchScreen) {
        this.launchScreen = launchScreen;

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

        assetManager = new AssetManager();

        for (String sound : sounds) {
            assetManager.load(sound, Sound.class);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        float progress = assetManager.getProgress();

        System.out.println(progress);

        if (assetManager.update()) {
            System.out.println("Assets loaded. Switching screens...");
            launchScreen.setScreen(new MainMenuScreen(launchScreen, assetManager));
        }
    }

    @Override
    public void dispose() {
        //TODO dispose resources
    }
}
