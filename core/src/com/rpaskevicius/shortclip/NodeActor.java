package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class NodeActor extends Actor {
    private Texture texture;
    private Sound sound;

    public NodeActor(float x, float y, String textureName, String soundName) {
        texture = new Texture(Gdx.files.internal(textureName));
        sound = Gdx.audio.newSound(Gdx.files.internal(soundName));

        setPosition(x, y);

        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());

        addListener(new NodeGestureListener(this));
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
}
