package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PianoRollActor extends NetworkedDuoplexedActor implements TimeListener, VisualOrigin {
    private Texture background;
    private Texture overlay;

    private int stepCount = 16;

    private boolean[][] steps = new boolean[8][];
    private Texture stepTexture = new Texture(Gdx.files.internal("piano-roll-pad-blue.png"));

    private float effectiveArea;

    private PianoRollGestureListener listener;

    private InstrumentActor instrument;
    private int currentIndex = -1;

    private int[] scaleMap = ScaleMap.genScaleMap(ScaleMap.defaultScale);

    private float markerOffset;
    private Texture markerTexture = new Texture(Gdx.files.internal("piano-roll-marker.png"));

    private TextButton scaleButton;

    public PianoRollActor(String ID, float x, float y, ShortClip currentScreen) {
        super(ID, 0);

        background = new Texture(Gdx.files.internal("piano-roll-background.png"));
        overlay = new Texture(Gdx.files.internal("piano-roll-overlay.png"));

        setBound(background.getWidth() - 32);

        setPosition(x, y);

        setBounds(getX(), getY(), background.getWidth(), background.getHeight());

        this.listener = new PianoRollGestureListener(this, currentScreen);
        addListener(this.listener);

        for (int i = 0; i < 8; i++) {
            steps[i] = new boolean[stepCount];
        }

        effectiveArea = background.getWidth() - 32; // TODO similar to bound, resolve

        //Scale selector
        Skin skin = new Skin();

        skin.add("default", new BitmapFont());
        skin.add("scale-button-texture", new Texture(Gdx.files.internal("ui-scale-button.png")));

        TextButton.TextButtonStyle scaleSelectorStyle = new TextButton.TextButtonStyle();
        scaleSelectorStyle.up = skin.newDrawable("scale-button-texture", Color.WHITE);
        scaleSelectorStyle.down = skin.newDrawable("scale-button-texture", Color.LIGHT_GRAY);
        scaleSelectorStyle.font = skin.getFont("default");
        skin.add("scale-button", scaleSelectorStyle);

        scaleButton = new TextButton(ScaleMap.defaultScale, skin, "scale-button");
        scaleButton.addListener(new ScaleButtonListener(this, currentScreen));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        scaleButton.setPosition(
            getX() + getWidth() - scaleButton.getWidth(),
            getY() + getHeight() - scaleButton.getHeight()
        );
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

        if (instrument != null) {
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

    public void onNextMarkerPosition(float ratio) {
        markerOffset = getEffectiveArea() * ratio;
    }

    @Override
    public boolean hasReference() { return (instrument != null); }

    @Override
    public VisualTarget getReference() { return instrument; }

    @Override
    public Vector2 getConnectionPoint() {
        float pointX = getWidth() - 16.0f;
        float pointY = getHeight() / 2.0f;

        return localToStageCoordinates(new Vector2(pointX, pointY));
    }

    @Override
    public boolean isInitiatingConnection() { return listener.isInitiatingConnection(); }

    @Override
    public Vector2 getCursorPosition() { return listener.getCursorPosition(); }

    public TextButton getScaleButton() { return scaleButton; }

    public void setScaleMap(int[] scaleMap) { this.scaleMap = scaleMap; }

}
