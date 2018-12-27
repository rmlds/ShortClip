package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LineActor extends Actor {
    private Texture texture;

    private Path<Vector2> path;

    private final Vector2 currentVec = new Vector2();
    private final Vector2 nextVec = new Vector2();

    private float resolution = 0.01f;

    private Vector2 start;
    private Vector2 midLower;
    private Vector2 midUpper;
    private Vector2 end;

    public LineActor(Vector2 start, Vector2 end, String textureName) {
        texture = new Texture(Gdx.files.internal(textureName));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        setPosition(start.x, start.y);

        setBounds(getX(), getY(), 1, 1);

        this.start = start;
        this.midLower = new Vector2(0, start.y);
        this.midUpper = new Vector2(0, end.y);
        this.end = end;

        updateMidPoints();

        path = new Bezier<Vector2>(start, midLower, midUpper, end);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        float val = 0f;

        while (val < 1f) {
            path.valueAt(/* out: */ currentVec, val);
            path.valueAt(/* out: */ nextVec, val + resolution);

            float deltaX = nextVec.x - currentVec.x;
            float deltaY = nextVec.y - currentVec.y;

            float angleInRadians = MathUtils.atan2(deltaY, deltaX);
            float angleInDegrees = angleInRadians * 180.0f / MathUtils.PI;

            float width = deltaY / MathUtils.sin(angleInRadians);

            drawLineSegment(batch, currentVec.x, currentVec.y, width + 0.1f, angleInDegrees);

            val += resolution;
        }
    }

    private void drawLineSegment(Batch batch, float x, float y, float width, float rotation) {
        batch.draw(texture, x, y-2.5f, 0, 2.5f, width, 5, 1, 1, rotation, 0, 0, 1, 5, false, false);
    }

    private void updateMidPoints() {
        float distance = Math.abs(start.x - end.x);
        float halfDistance = distance / 2.0f;

        midLower.x = start.x + halfDistance;
        midUpper.x = end.x - halfDistance;
    }

    public void setStart(Vector2 start) {
        this.start = start;

        updateMidPoints();
    }

    public void setEnd(Vector2 end) {
        this.end = end;

        updateMidPoints();
    }

    public Vector2 getEnd() {
        return end;
    }
}
