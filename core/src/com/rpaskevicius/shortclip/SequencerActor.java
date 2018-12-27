package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

import static com.badlogic.gdx.utils.TimeUtils.millis;

public class SequencerActor extends Actor {
    private Texture texture;

    private int stepCount;
    private boolean[] steps;
    private Texture stepTexture = new Texture(Gdx.files.internal("pad-purple.png"));

    private NodeActor node;

    private int currentIndex;

    private ShapeRenderer renderer = new ShapeRenderer();

    public SequencerActor(float x, float y, String textureName, int stepCount, Stage stage) {
        texture = new Texture(Gdx.files.internal(textureName));

        setPosition(x, y);

        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());

        addListener(new SequencerGestureListener(this, stage));

        this.stepCount = stepCount;
        steps = new boolean[stepCount];

        currentIndex = -1; //otherwise the 0th step at the very start won't be triggered. TODO Should also do this on time.stop();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());

        float stepWidth = getWidth() / getStepCount();

        //draw individual steps
        for (int i = 0; i < stepCount; i++) {
            if (steps[i]) {
                batch.draw(stepTexture, getX() + (i * stepWidth), getY());
            }
        }

        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.line(getX(), getY(), node.getX(), node.getY()); // only for debug, bad berformance
        renderer.end();

        batch.begin();
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

        if (steps[stepIndex]) {
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
}
