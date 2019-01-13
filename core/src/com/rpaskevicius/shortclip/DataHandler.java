package com.rpaskevicius.shortclip;

import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DataHandler extends NetworkHandler {

    private ShortClip currentScreen;

    public DataHandler(Socket socket, ShortClip currentScreen) {
        super(socket);
        this.currentScreen = currentScreen;
    }

    @Override
    protected void handleMessage() {
        byte action = message.getAction();
        byte param = message.getParam();

        if (action == 0) { //something about nodes
            if (param == 0) {
                //create new node

                String nodeID = message.readStr(8);

                System.out.println("New node ID: " + nodeID);

                System.out.println("Debug: " + message.debug(17));

                int x = message.readInt(8);
                int y = message.readInt(12);

                System.out.println("Received message to create new node. x: " + x + " y: " + y);

                NodeActor node = new NodeActor(nodeID, x, y, "node-purple-w-connector.png", "kick-01.wav", currentScreen.getAssetManager(), currentScreen.getCenterUI(), currentScreen);
                currentScreen.getStage().addActor(node);

            } else if (param == 1) {
                //update existing node position

                String nodeID = message.readStr(8);

                System.out.println("Existing node ID: " + nodeID);

                System.out.println("Debug: " + message.debug(17));

                int x = message.readInt(8);
                int y = message.readInt(12);

                System.out.println("Received message to update existing node position. x: " + x + " y: " + y);

                for (Actor actor : currentScreen.getStage().getActors()) {
                    if (actor instanceof NodeActor) {

                        System.out.println("actor instanceof NodeActor. nodeID: " + ((NodeActor) actor).getNodeID());

                        if (((NodeActor) actor).getNodeID().equals(nodeID)) {
                            System.out.println("Found node. setPosition x: " + x + " y: " + y);
                            actor.setPosition(x, y);

                            return;
                        }
                    }
                }

                System.out.println("Node not found. nodeID: " + nodeID);

            } else if (param == 2) {
                //update existing node sound

                String nodeID = message.readStr(8);

                System.out.println("Existing node ID: " + nodeID);

                System.out.println("Debug: " + message.debug(17));

                byte sound = message.readByte(16);

                System.out.println("Received message to update existing node sound. sound: " + sound);

                for (Actor actor : currentScreen.getStage().getActors()) {
                    if (actor instanceof NodeActor) {

                        System.out.println("actor instanceof NodeActor. nodeID: " + ((NodeActor) actor).getNodeID());

                        if (((NodeActor) actor).getNodeID().equals(nodeID)) {
                            String soundString = AssetMap.getNodeSoundString((int)sound);

                            System.out.println("Found node. setSound: " + sound + " -> " + soundString);

                            ((NodeActor) actor).setSound(soundString);

                            ((NodeActor) actor).getNodeGestureListener().getList().setSelected(soundString);

                            return;
                        }
                    }
                }

                System.out.println("Node not found. nodeID: " + nodeID);

            } else if (param == 44) {
                //node not found

            } else if (param == 40) {
                //user requested an invalid param
            } else {
                //server sent an invalid param
            }
        } else if (action == 1) { //something about sequencers
            if (param == 0) {
                //create new sequencer

                String sequencerID = message.readStr(8);

                System.out.println("New sequencer ID: " + sequencerID);

                System.out.println("Debug: " + message.debug(18));

                int x = message.readInt(8);
                int y = message.readInt(12);

                System.out.println("Received message to create new sequencer. x: " + x + " y: " + y);

                SequencerActor sequencer = new SequencerActor(sequencerID, x, y, "sequencer-grey-w-panel-white.png", 16, 32, currentScreen.getStage());

                currentScreen.getTimeDispatcher().addListener(sequencer);
                currentScreen.getStage().addActor(sequencer);
            }

        } else if (action == 40) {
            //user requested an invalid action
        } else {
            //server sent an invalid action
        }
    }
}
