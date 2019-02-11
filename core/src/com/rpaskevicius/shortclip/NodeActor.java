package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class NodeActor extends NetworkedActor implements VisualTarget {
    private Texture texture;
    private Sound sound;

    private AssetManager assetManager;

    private NodeGestureListener nodeGestureListener;

    private float volume = 1.0f;

    private TextButton volumeSelector;
    private VolumeSelectorListener volumeSelectorListener;

    public NodeActor(String ID, float x, float y, String textureName, String soundName, AssetManager assetManager, ShortClip currentScreen) {
        super(ID);
        this.assetManager = assetManager;

        texture = new Texture(Gdx.files.internal(textureName));

        setSound(soundName);

        setPosition(x, y);

        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());

        this.nodeGestureListener = new NodeGestureListener(this, currentScreen);
        addListener(this.nodeGestureListener);

        //Volume button
        Skin skin = new Skin();

        skin.add("default", new BitmapFont());
        skin.add("volume-selector-texture", new Texture(Gdx.files.internal("ui-bpm.png")));

        TextButton.TextButtonStyle volumeSelectorStyle = new TextButton.TextButtonStyle();
        volumeSelectorStyle.up = skin.newDrawable("volume-selector-texture", Color.WHITE);
        volumeSelectorStyle.down = skin.newDrawable("volume-selector-texture", Color.LIGHT_GRAY);
        volumeSelectorStyle.font = skin.getFont("default");
        skin.add("volume-selector", volumeSelectorStyle);

        volumeSelector = new TextButton(Integer.toString((int)(volume * 100.0f)), skin, "volume-selector");

        this.volumeSelectorListener = new VolumeSelectorListener(this, currentScreen);
        volumeSelector.addListener(this.volumeSelectorListener);

    }

    public void play(){
        sound.play(volume);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        volumeSelector.setPosition(
                getX() + getWidth() / 2.0f,
                getY() + getHeight() / 2.0f
        );
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    public void setSound(String sound) {
        this.sound = assetManager.get(sound, Sound.class);
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

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public TextButton getVolumeSelector() {
        return volumeSelector;
    }

    public VolumeSelectorListener getVolumeSelectorListener() {
        return volumeSelectorListener;
    }

}
