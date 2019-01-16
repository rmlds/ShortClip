package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class SequencerGestureListener extends ActorGestureListener {

    private ShortClip currentScreen;

    private SequencerActor sequencerActor;

    private boolean initiatingConnection;

    private Vector2 cursorPosition = new Vector2(0, 0);

    public SequencerGestureListener(ShortClip currentScreen, SequencerActor sequencerActor) {
        this.currentScreen = currentScreen;
        this.sequencerActor = sequencerActor;
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

            //send steps to server
            NetworkMessage message = new NetworkMessage();
            message.build(1, 2, 24);
            message.writeStr(sequencerActor.getSequencerID());
            message.writeBoolArr(sequencerActor.getSteps(), 8);

            System.out.println("Sending message to update sequencer steps. Debug: ");
            System.out.println(message.debug(24));

            currentScreen.getDataHandler().writeMessage(message);
        }
    }

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        //Inside effective area OR Inside panel area

        //If the effective area was clicked, move the object
        if (x < sequencerActor.getEffectiveArea() && !initiatingConnection) {
            float newX = sequencerActor.getX() + deltaX;
            float newY = sequencerActor.getY() + deltaY;

            sequencerActor.setPosition(newX, newY);

            System.out.println("Move actor: " + sequencerActor.getX() + " " + sequencerActor.getY());

            //send the new position to the server
            NetworkMessage message = new NetworkMessage();
            message.build(1, 1, 16);
            message.writeStr(sequencerActor.getSequencerID());
            message.writeInt((int)newX, 8);
            message.writeInt((int)newY, 12);

            System.out.println("Sending message to update sequencer position. Debug: ");
            System.out.println(message.debug(16));

            currentScreen.getDataHandler().writeMessage(message);

        } else { // outside of effective area
            //TODO update connection line
            cursorPosition = sequencerActor.localToStageCoordinates(new Vector2(x, y));
        }

        event.handle(); //inform Stage that this event has been handled
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Touch down: " + x + " " + y);

        if (x > sequencerActor.getEffectiveArea()) {
            initiatingConnection = true;

            //TODO start drawing connection line
            cursorPosition = sequencerActor.localToStageCoordinates(new Vector2(x, y));

            //Vector2 lineStart = sequencerActor.getConnectionPoint();
            //Vector2 lineEnd = sequencerActor.localToStageCoordinates(new Vector2(x, y));
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Touch up: " + x + " " + y);

        //look for actors. If there is one, connect to it
        if (initiatingConnection) {
            Vector2 hitPoint = sequencerActor.localToStageCoordinates(new Vector2(x, y));

            Actor hitResult = sequencerActor.getStage().hit(hitPoint.x, hitPoint.y, false);

            if (hitResult == null) {
                //No actor was hit. Clear node reference if it exists.
                clearNode();

            } else {
                //An actor was hit.

                if (hitResult instanceof NodeActor) {
                    NodeActor node = (NodeActor) hitResult;

                    //Assign the new references
                    sequencerActor.clearNode();
                    sequencerActor.setNode(node);

                    //send the references to the server
                    NetworkMessage message = new NetworkMessage();
                    message.build(1, 3, 16);
                    message.writeStr(sequencerActor.getSequencerID());
                    message.writeStr(node.getNodeID(), 8);

                    System.out.println("Sending message to link to node. Debug: ");
                    System.out.println(message.debug(16));

                    currentScreen.getDataHandler().writeMessage(message);

                } else {
                    //The actor is not NodeActor. Pretend as if no actor was hit and clear node reference.
                    clearNode();
                }
            }

            initiatingConnection = false;
        }
    }

    public boolean isInitiatingConnection() {
        return this.initiatingConnection;
    }

    public Vector2 getCursorPosition() {
        return cursorPosition;
    }

    private void clearNode() {
        sequencerActor.clearNode();

        NetworkMessage message = new NetworkMessage();
        message.build(1, 4, 8);
        message.writeStr(sequencerActor.getSequencerID());

        System.out.println("Sending message to remove link. Debug: ");
        System.out.println(message.debug(8));

        currentScreen.getDataHandler().writeMessage(message);
    }
}
