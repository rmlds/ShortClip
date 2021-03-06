package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class InstrumentActor extends NetworkedActor implements VisualTarget, VolumeActor {
    private Texture background;
    private Sound tones[] = new Sound[24];

    private AssetManager assetManager;

    private InstrumentGestureListener listener;

    private float volume = 1.0f;

    private TextButton volumeSelector;
    private VolumeSelectorListener volumeSelectorListener;

    public InstrumentActor(String ID, float x, float y, ShortClip screen) {
        super(ID);

        this.assetManager = screen.getAssetManager();

        background = new Texture(Gdx.files.internal("node-blue.png"));

        setPosition(x, y);

        setBounds(getX(), getY(), background.getWidth(), background.getHeight());

        setInstrument(AssetMap.defaultInstrument);

        this.listener = new InstrumentGestureListener(this, screen);
        addListener(this.listener);

        //Volume selector
        volumeSelector = UIGenerator.genVolumeSelector();

        this.volumeSelectorListener = new VolumeSelectorListener(this, screen);
        volumeSelector.addListener(this.volumeSelectorListener);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        volumeSelector.setPosition(
                getX() + getWidth() / 2.0f + volumeSelector.getWidth() / 2.0f,
                getY() + getHeight() / 2.0f - volumeSelector.getHeight() / 2.0f
        );
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(background, getX(), getY());
    }

    public void play(int tone){
        tones[tone].play(volume);
    }

    public void setInstrument(String instrument) {
        //load tones
        for (int i = 0; i < 24; i++) {
            String fileName = instrument + "/" + instrument + "-" + i + ".wav";

            this.tones[i] = assetManager.get(fileName, Sound.class);
        }
    }

    @Override
    public Vector2 getConnectionPoint() {
        float pointX = 16.0f;
        float pointY = getHeight() / 2.0f;

        return localToStageCoordinates(new Vector2(pointX, pointY));
    }

    public InstrumentGestureListener getListener() {
        return listener;
    }

    public int getVolume() {
        return (int)(volume * 100.0f);
    }

    public void setVolume(int volume) {
        if (volume < 0) {
            volume = 0;
        } else if (volume > 100) {
            volume = 100;
        }

        this.volume = (float)(volume) / 100.0f;
    }

    public TextButton getVolumeSelector() {
        return volumeSelector;
    }

    public VolumeSelectorListener getVolumeSelectorListener() {
        return volumeSelectorListener;
    }

}
