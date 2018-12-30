package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class NodeActor extends Actor {
    private Texture texture;
    private Sound sound;

    private SequencerActor sequencer;

    private AssetManager assetManager;

    public NodeActor(float x, float y, String textureName, String soundName, AssetManager assetManager, Table centerUI) {
        texture = new Texture(Gdx.files.internal(textureName));
        sound = Gdx.audio.newSound(Gdx.files.internal(soundName));

        setPosition(x, y);

        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());

        addListener(new NodeGestureListener(this, assetManager, centerUI));

        this.assetManager = assetManager;
    }

    public void play(){
        sound.play(1.0f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    public void setSequencer(SequencerActor sequencer) {
        this.sequencer = sequencer;
    }

    public void clearSequencer() {
        this.sequencer = null;
    }

    public boolean hasSequencer() {
        return (this.sequencer != null);
    }

    public SequencerActor getSequencer() {
        return this.sequencer;
    }

    public Vector2 getConnectionPoint() {
        float pointX = 16.0f;
        float pointY = getHeight() / 2.0f;

        return localToStageCoordinates(new Vector2(pointX, pointY));
    }

    public void setSound(String sound) {
        this.sound = assetManager.get(sound, Sound.class);
    }
}
