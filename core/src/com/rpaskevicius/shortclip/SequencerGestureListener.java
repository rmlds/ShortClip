package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class SequencerGestureListener extends ActorGestureListener {

    private SequencerActor sequencerActor;

    private boolean initiatingConnection;
    private boolean hasConnection;

    private ShortClip currentScreen;

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

            //TODO send steps to server
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

            //TODO send the new position to the server ~~~~~~~~~~~~~
            NetworkMessage message = new NetworkMessage();
            message.build(1, 1, 16);
            message.writeStr(sequencerActor.getSequencerID());
            message.writeInt((int)newX, 8);
            message.writeInt((int)newY, 12);

            System.out.println("Sending message to update sequencer position. Debug: ");
            System.out.println(message.debug(16));

            currentScreen.getDataHandler().writeMessage(message);

            //If there is a line, we also need to update its starting position
            if (sequencerActor.hasLine()) {
                Vector2 lineStart = sequencerActor.getConnectionPoint();

                sequencerActor.getLine().setStart(lineStart);
            }

            System.out.println("Move actor: " + sequencerActor.getX() + " " + sequencerActor.getY());
        } else { // outside of effective area

            if (sequencerActor.hasLine()) {
                //update the existing line
                Vector2 lineEnd = sequencerActor.localToStageCoordinates(new Vector2(x, y));
                System.out.println("Update line: " + lineEnd.x + " " + lineEnd.y);

                sequencerActor.getLine().setEnd(lineEnd);
            }
        }

        event.handle(); //inform Stage that this event has been handled
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Touch down: " + x + " " + y);

        if (x > sequencerActor.getEffectiveArea()) {
            initiatingConnection = true;

            //If there is no line, create it
            if (!sequencerActor.hasLine()) {
                Vector2 lineStart = sequencerActor.getConnectionPoint();
                Vector2 lineEnd = sequencerActor.localToStageCoordinates(new Vector2(x, y));

                sequencerActor.createLine(lineStart, lineEnd);
            }

        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Touch up: " + x + " " + y);

        //look for actors. If there is one, connect to it
        if (initiatingConnection) {
            Vector2 hitPoint = sequencerActor.localToStageCoordinates(new Vector2(x, y));

            Actor hitResult = sequencerActor.getStage().hit(hitPoint.x, hitPoint.y, false); //TODO could getStage() return null?

            if (hitResult == null) {
                //No actor was hit. Remove the line, and clear references if they exist.

                sequencerActor.disposeLine();

                clearRefsOnSequencer();
            } else {
                //An actor was hit.

                if (hitResult instanceof NodeActor) {
                    NodeActor node = (NodeActor) hitResult;

                    sequencerActor.disposeLine();

                    //If this sequencer had a connection, clear all references
                    if (sequencerActor.hasNode()) {
                        //TODO Is this removal actually needed? (when switching from one node to another)
                        sequencerActor.getNode().clearSequencer();
                        sequencerActor.clearNode();
                    } //TODO this awfully looks like clearRefsOnSequencer()

                    //If hitResult node had a connection, clear all references. Also remove line.
                    if (node.hasSequencer()) {
                        node.getSequencer().disposeLine();

                        node.getSequencer().clearNode();
                        node.clearSequencer();
                    }

                    //Assign the new references
                    node.setSequencer(sequencerActor);
                    sequencerActor.setNode(node);

                    //TODO send the references to the server
                    NetworkMessage message = new NetworkMessage();
                    message.build(1, 3, 16);
                    message.writeStr(sequencerActor.getSequencerID());
                    message.writeStr(node.getNodeID(), 8);

                    System.out.println("Sending message to link to node. Debug: ");
                    System.out.println(message.debug(16));

                    currentScreen.getDataHandler().writeMessage(message);

                    //Draw a permanent line
                    Vector2 lineStart = sequencerActor.getConnectionPoint();
                    Vector2 lineEnd = sequencerActor.getNode().getConnectionPoint();

                    sequencerActor.createLine(lineStart, lineEnd);

                } else {
                    //clearLine(); //TODO will cause NullPointerEx if line does not exist. To fix, create the line in touchDown instead.
                    sequencerActor.disposeLine();

                    clearRefsOnSequencer(); //The actor is not NodeActor. Pretend as if no actor was hit and clear references.
                }
            }

            //TODO this is a huge mess. clearRefsOnSequencer() and disposeLine() seems to be called in every possible case.

            initiatingConnection = false;
        }
    }

    private void clearRefsOnSequencer() {
        if (sequencerActor.hasNode()) {
            sequencerActor.getNode().clearSequencer();
            sequencerActor.clearNode();
        }
    }

}
