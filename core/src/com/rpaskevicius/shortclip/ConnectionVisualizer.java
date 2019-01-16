package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ConnectionVisualizer {

    private ShortClip currentScreen;

    private Texture lineTexture = new Texture(Gdx.files.internal("line-segment.png"));

    private Vector2 start = new Vector2();
    private Vector2 midLower = new Vector2();
    private Vector2 midUpper = new Vector2();
    private Vector2 end = new Vector2();

    private Path<Vector2> path = new Bezier<Vector2>(start, midLower, midUpper, end);

    private float resolution = 0.01f;

    private final Vector2 currentVec = new Vector2();
    private final Vector2 nextVec = new Vector2();

    public ConnectionVisualizer(ShortClip currentScreen) {
        this.currentScreen = currentScreen;
    }

    public void drawConnections(Batch batch) {
        batch.begin();

        for (Actor actor : currentScreen.getStage().getActors()) {
            if (actor instanceof SequencerActor) {
                SequencerActor sequencer = (SequencerActor) actor;

                if (sequencer.hasNode()) {
                    NodeActor node = sequencer.getNode();

                    Vector2 sequencerConnPoint = sequencer.getConnectionPoint();
                    Vector2 nodeConnPoint = node.getConnectionPoint();

                    start.x = sequencerConnPoint.x;
                    start.y = sequencerConnPoint.y;

                    end.x = nodeConnPoint.x;
                    end.y = nodeConnPoint.y;

                    updateMidPoints();

                    //Coordinates are set up. Now we can draw the path
                    drawLine(batch);
                }

                //if user is initiating connection, draw line from sequencer to cursor position
                if (sequencer.isInitiatingConnection()) {
                    Vector2 sequencerConnPoint = sequencer.getConnectionPoint();
                    Vector2 initPosition = sequencer.getCursorPosition();

                    start.x = sequencerConnPoint.x;
                    start.y = sequencerConnPoint.y;

                    end.x = initPosition.x;
                    end.y = initPosition.y;

                    updateMidPoints();

                    drawLine(batch);
                }

            }
        }

        batch.end();
    }

    private void drawLine(Batch batch) {
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
        batch.draw(lineTexture, x, y-2.5f, 0, 2.5f, width, 5, 1, 1, rotation, 0, 0, 1, 5, false, false);
    }

    private void updateMidPoints() {
        float halfDistance = Math.abs(start.x - end.x) / 2.0f;

        midLower.x = start.x + halfDistance;
        midLower.y = start.y;

        midUpper.x = end.x - halfDistance;
        midUpper.y = end.y;
    }

}
