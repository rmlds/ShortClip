package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class LaunchScreen extends Game {

    @Override
    public void create() {
//        new AssetListGenerator();

        this.setScreen(new AssetLoadingScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        super.render();
    }

    @Override
    public void dispose() {
        System.out.println("LaunchScreen dispose().");
    }
}
