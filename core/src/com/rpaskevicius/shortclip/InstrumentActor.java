package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InstrumentActor extends Actor {
    private Texture background;
    private Sound tones[] = new Sound[24];

    private AssetManager assetManager;

    private String nodeID;

    public InstrumentActor(String nodeID, float x, float y, ShortClip currentScreen) {
        this.nodeID = nodeID;
        this.assetManager = currentScreen.getAssetManager();

        background = new Texture(Gdx.files.internal("node-blue.png"));

        setPosition(x, y);

        setBounds(getX(), getY(), background.getWidth(), background.getHeight());

        setInstrument("bass");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(background, getX(), getY());
    }

    public void play(int tone){
        tones[tone].play(1.0f);
    }

    public void setInstrument(String instrument) {
        //load tones
        for (int i = 0; i < 24; i++) {
            String fileName = instrument + "/" + instrument + "-" + i + ".wav";

            this.tones[i] = assetManager.get(fileName, Sound.class);
        }
    }
}
