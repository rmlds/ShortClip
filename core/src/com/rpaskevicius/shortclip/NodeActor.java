package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class NodeActor extends Actor implements VisualTarget {
    private Texture texture;
    private Sound sound;

    private AssetManager assetManager;

    private String nodeID;

    private NodeGestureListener nodeGestureListener;

    public NodeActor(String nodeID, float x, float y, String textureName, String soundName, AssetManager assetManager, Table centerUI, ShortClip currentScreen) {
        this.nodeID = nodeID;
        this.assetManager = assetManager;

        texture = new Texture(Gdx.files.internal(textureName));

        setSound(soundName);

        setPosition(x, y);

        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());

        this.nodeGestureListener = new NodeGestureListener(this, assetManager, centerUI, currentScreen);
        addListener(this.nodeGestureListener);
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

    public void setSound(String sound) {
        this.sound = assetManager.get(sound, Sound.class);
    }

    public String getNodeID() {
        return nodeID;
    }

    public NodeGestureListener getNodeGestureListener() {
        return nodeGestureListener;
    }

    @Override
    public Vector2 getConnectionPoint() {
        float pointX = 16.0f;
        float pointY = getHeight() / 2.0f;

        return localToStageCoordinates(new Vector2(pointX, pointY));
    }
}
