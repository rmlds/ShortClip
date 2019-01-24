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

    public AssetLoadingScreen(Game launchScreen) {
        this.launchScreen = launchScreen;

        assetManager = new AssetManager();

        for (String sound : AssetMap.sounds) {
            assetManager.load(sound, Sound.class);
        }

        for (String instrument : AssetMap.instruments) {
            loadInstrument(instrument);
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

    private void loadInstrument(String instrument) {
        for (int i = 0; i < 24; i++) {
            String fileName = instrument + "/" + instrument + "-" + i + ".wav";

            assetManager.load(fileName, Sound.class);
        }
    }
}
