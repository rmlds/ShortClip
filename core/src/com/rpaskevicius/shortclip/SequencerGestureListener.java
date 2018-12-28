package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import javax.xml.soap.Node;

public class SequencerGestureListener extends ActorGestureListener {

    private SequencerActor sequencerActor;
    private Stage stage;

    LineActor line;

    private boolean initiatingConnection;
    private boolean hasConnection;

    public SequencerGestureListener(SequencerActor sequencerActor, Stage stage) {
        this.sequencerActor = sequencerActor;
        this.stage = stage;

//        Vector2 position = new Vector2(sequencerActor.getX(), sequencerActor.getY());
//        line = new LineActor(position, position, "line-segment.png");

//        this.stage.addActor(line);
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        // Toggle the steps, but only if the effective area was clicked
        if (x < sequencerActor.getEffectiveArea()) {
            float stepWidth = sequencerActor.getEffectiveArea() / sequencerActor.getStepCount();

            int stepIndex = (int) (x / stepWidth);

            if (sequencerActor.getStepState(stepIndex)) {
                sequencerActor.setStepState(stepIndex, false);
            } else {
                sequencerActor.setStepState(stepIndex, true);
            }

            System.out.println("Tap: " + x + " " + y + " " + stepIndex);
        }
    }

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        /*
        ~~Inside effective area
         ~

        ~~Inside panel area
         */

        //If the effective area was clicked, move the object
        if (x < sequencerActor.getEffectiveArea() && !initiatingConnection) {
            sequencerActor.setPosition(sequencerActor.getX() + deltaX, sequencerActor.getY() + deltaY);

            if (line != null) {
                float lineStartX = sequencerActor.getEffectiveArea() + (sequencerActor.getPanelArea() / 2.0f);
                float lineStartY = sequencerActor.getHeight() / 2.0f;

                Vector2 lineStart = sequencerActor.localToStageCoordinates(new Vector2(lineStartX, lineStartY));

                line.setStart(lineStart);
            }

            System.out.println("Move actor: " + sequencerActor.getX() + " " + sequencerActor.getY());
        } else { // if outside of effective area, initiate a new line
            //initiatingConnection = true; //TODO this probably belongs in touchDown, because of event order.

            if (line == null) {
                //create a line
                float lineStartX = sequencerActor.getEffectiveArea() + (sequencerActor.getPanelArea() / 2.0f);
                float lineStartY = sequencerActor.getHeight() / 2.0f;

                Vector2 lineStart = sequencerActor.localToStageCoordinates(new Vector2(lineStartX, lineStartY));
                Vector2 lineEnd = sequencerActor.localToStageCoordinates(new Vector2(x, y));

		        line = new LineActor(lineStart, lineEnd, "line-segment.png");
		        stage.addActor(line);

            } else {
                //update the existing line
                Vector2 lineEnd = sequencerActor.localToStageCoordinates(new Vector2(x, y));
                System.out.println("Update line: " + lineEnd.x + " " + lineEnd.y);

                line.setEnd(lineEnd);
            }
        }

        event.handle(); //inform Stage that this event has been handled
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Touch down: " + x + " " + y);

        if (x > sequencerActor.getEffectiveArea()) {
            initiatingConnection = true;
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Touch up: " + x + " " + y);

        //look for actors. If there is one, connect to it
        if (initiatingConnection) {
            Vector2 lineEnd = sequencerActor.localToStageCoordinates(new Vector2(x, y));

            Actor hitResult = stage.hit(lineEnd.x, lineEnd.y, false);

            if (hitResult == null) {
                //No actor was hit. Remove the line, and clear references if they exist.

                clearLine();

                clearRefsOnSequencer(); //Clear references
            } else {
                //An actor was hit.

                if (hitResult instanceof NodeActor) {
                    NodeActor node = (NodeActor) hitResult;

                    //If this sequencer had a connection, clear all references
                    if (sequencerActor.hasNode()) {
                        //TODO Is this removal actually needed? (when switching from one node to another)
                        sequencerActor.getNode().clearSequencer();
                        sequencerActor.clearNode();
                    } //TODO this awfully looks like clearRefsOnSequencer()

                    //If hitResult node had a connection, clear all references
                    if (node.hasSequencer()) {
                        node.getSequencer().clearNode();
                        node.clearSequencer();
                    }

                    //Assign the new references
                    node.setSequencer(sequencerActor);
                    sequencerActor.setNode(node);

                } else {
                    clearLine(); //TODO will cause NullPointerEx if line does not exist. To fix, create the line in touchDown instead.

                    clearRefsOnSequencer(); //The actor is not NodeActor. Pretend as if no actor was hit and clear references.
                }
            }

            //TODO this is a huge mess. clearRefsOnSequencer() seems to be called in every possible case.

            initiatingConnection = false;
        }
    }

    private void clearLine() {
        line.remove(); // remove the line from stage
        line = null;
    }

    private void clearRefsOnSequencer() {
        if (sequencerActor.hasNode()) {
            sequencerActor.getNode().clearSequencer();
            sequencerActor.clearNode();
        }
    }


}
