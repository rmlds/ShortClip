package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class SequencerActor extends Actor {
    private Texture texture;

    private int stepCount;

    private boolean[] steps;
    private Texture stepTexture = new Texture(Gdx.files.internal("pad-purple.png"));

    private NodeActor node;

    private int currentIndex;

    private float effectiveArea;
    private float panelArea;

    private LineActor line;
    private Stage stage;

    private float markerPosition;
    private Texture markerTexture = new Texture(Gdx.files.internal("marker-white.png"));

    private Texture stepHolderTexture = new Texture(Gdx.files.internal("step-holder.png"));
    private Texture stepHolderSmallTexture = new Texture(Gdx.files.internal("step-holder-small.png"));

    private float stepHolderOffsetY;
    private float stepHolderSmallOffsetY;

    private String sequencerID;

    private SequencerGestureListener sequencerGestureListener;

    public SequencerActor(String sequencerID, float x, float y, String textureName, int stepCount, float panelWidth, Stage stage, ShortClip currentScreen) {
        this.sequencerID = sequencerID;
        texture = new Texture(Gdx.files.internal(textureName));

        setPosition(x, y);

        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());

        this.sequencerGestureListener = new SequencerGestureListener(currentScreen, this);
        addListener(this.sequencerGestureListener);

        this.stage = stage;

        this.stepCount = stepCount;
        steps = new boolean[stepCount];

        currentIndex = -1; //otherwise the 0th step at the very start won't be triggered. TODO Should also do this on time.stop();

        panelArea = panelWidth;
        effectiveArea = texture.getWidth() - panelWidth;

        stepHolderOffsetY = (getHeight() - stepHolderTexture.getHeight()) / 2.0f;
        stepHolderSmallOffsetY = (getHeight() - stepHolderSmallTexture.getHeight()) / 2.0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());

        float stepWidth = getEffectiveArea() / getStepCount();

        for (int i = 0; i < stepCount; i++) {
            //draw individual steps
            if (steps[i]) {
                batch.draw(stepTexture, getX() + (i * stepWidth), getY());
            }

            //draw step holders
            if (i % 4 == 0) {
                batch.draw(stepHolderTexture, getX() + (i * stepWidth), getY() + stepHolderOffsetY);
            } else {
                batch.draw(stepHolderSmallTexture, getX() + (i * stepWidth), getY() + stepHolderSmallOffsetY);
            }
        }

        //draw the playback marker
        batch.draw(markerTexture, markerPosition, getY());
    }

    public int getStepCount() {
        return stepCount;
    }

    public boolean getStepState(int index) {
        return steps[index];
    }

    public void setStepState(int index, boolean state) {
        steps[index] = state;
    }

    public void onNextStep(int stepIndex) {
        setCurrentIndex(stepIndex);

        //System.out.println("onNextStep");

        if (node != null && steps[stepIndex]) {
            node.play();
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setNode(NodeActor node) {
        this.node = node;
    }

    public void clearNode() {
        this.node = null;
    }

    public boolean hasNode() {
        return (this.node != null);
    }

    public NodeActor getNode() {
        return this.node;
    }

    public float getEffectiveArea() {
        return effectiveArea;
    }

    public float getPanelArea() {
        return panelArea;
    }

    public Vector2 getConnectionPoint() {
        float pointX = getWidth() - 16.0f;
        float pointY = getHeight() / 2.0f;

        return localToStageCoordinates(new Vector2(pointX, pointY));
    }

    public void onNextMarkerPosition(long sequencePartial, long sequenceDuration) {
        float ratio = (float) sequencePartial / (float) sequenceDuration;

        markerPosition = getX() + (getEffectiveArea() * ratio);
    }

    public String getSequencerID() {
        return this.sequencerID;
    }

    public boolean[] getSteps() {
        return this.steps;
    }

    public void setSteps(boolean[] steps) {
        for (int i = 0; i < steps.length; i++) {
            this.steps[i] = steps[i];
        }
    }

    public boolean isInitiatingConnection() {
        return this.sequencerGestureListener.isInitiatingConnection();
    }

    public Vector2 getCursorPosition() {
        return this.sequencerGestureListener.getCursorPosition();
    }
}
