package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PianoRollActor extends NetworkedActor implements TimeListener {
    private Texture background;
    private Texture overlay;

    private int stepCount = 16;

    private boolean[][] steps = new boolean[8][];
    private Texture stepTexture = new Texture(Gdx.files.internal("piano-roll-pad.png"));

    private float effectiveArea;

    private PianoRollGestureListener pianoRollGestureListener;

    private InstrumentActor instrument;
    private int currentIndex = -1;

    private int[] scaleMap = ScaleMap.genScaleMap("e");

    private float markerOffset;
    private Texture markerTexture = new Texture(Gdx.files.internal("piano-roll-marker.png"));

    public PianoRollActor(String ID, float x, float y, ShortClip currentScreen) {
        super(ID);

        background = new Texture(Gdx.files.internal("piano-roll-background.png"));
        overlay = new Texture(Gdx.files.internal("piano-roll-overlay.png"));

        setPosition(x, y);

        setBounds(getX(), getY(), background.getWidth(), background.getHeight());

        this.pianoRollGestureListener = new PianoRollGestureListener(currentScreen, this);
        addListener(this.pianoRollGestureListener);

        for (int i = 0; i < 8; i++) {
            steps[i] = new boolean[stepCount];
        }

        effectiveArea = background.getWidth() - 32; //panel width = 32
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(background, getX(), getY());

        float stepWidth = getEffectiveArea() / getStepCount();
        float stepHeight = 32.0f;

        for (int toneIndex = 0; toneIndex < 8; toneIndex++) {
            for (int stepIndex = 0; stepIndex < stepCount; stepIndex++) {
                if (steps[toneIndex][stepIndex]) {
                    batch.draw(stepTexture,
                            getX() + (stepIndex * stepWidth),
                            getY() + (toneIndex * stepHeight)
                    );
                }
            }
        }

        batch.draw(overlay, getX(), getY());

        //draw the playback marker
        batch.draw(markerTexture, getX() + markerOffset, getY());
    }

    public float getEffectiveArea() {
        return effectiveArea;
    }

    public int getStepCount() {
        return stepCount;
    }

    public boolean getStepState(int toneIndex, int stepIndex) {
        return steps[toneIndex][stepIndex];
    }

    public void setStepState(int toneIndex, int stepIndex, boolean state) {
        this.steps[toneIndex][stepIndex] = state;
    }

    public void onNextStep(int stepIndex) {
        setCurrentIndex(stepIndex);

        if (instrument != null){
            for (int toneIndex = 0; toneIndex < 8; toneIndex++) {
                if (steps[toneIndex][stepIndex]) {
                    //scaleMap[toneIndex] maps the toneIndex (0-7) to a scale (0-24)
                    instrument.play(scaleMap[toneIndex]);
                }
            }
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setInstrument(InstrumentActor instrument) {
        this.instrument = instrument;
    }

    public void clearInstrument() {
        this.instrument = null;
    }

    public boolean hasInstrument() {
        return (this.instrument != null);
    }

    public InstrumentActor getInstrument() {
        return this.instrument;
    }

    public void onNextMarkerPosition(float ratio) {
        markerOffset = getEffectiveArea() * ratio;
    }
}
