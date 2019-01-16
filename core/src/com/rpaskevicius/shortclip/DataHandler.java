package com.rpaskevicius.shortclip;

import com.badlogic.gdx.net.Socket;

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

                int x = message.readInt(8);
                int y = message.readInt(12);

                NodeActor node = new NodeActor(nodeID, x, y, "node-purple-w-connector.png", "kick-01.wav", currentScreen.getAssetManager(), currentScreen.getCenterUI(), currentScreen);
                currentScreen.getStage().addActor(node);

            } else if (param == 1) {
                //update existing node position

                String nodeID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                NodeActor node = currentScreen.getNodeByID(nodeID);

                if (node != null) { node.setPosition(x, y); }

            } else if (param == 2) {
                //update existing node sound

                String nodeID = message.readStr(8);

                byte sound = message.readByte(16);

                NodeActor node = currentScreen.getNodeByID(nodeID);

                if (node != null) {
                    String soundString = AssetMap.getNodeSoundString((int)sound);

                    node.setSound(soundString);

                    node.getNodeGestureListener().getList().setSelected(soundString);
                }

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

                int x = message.readInt(8);
                int y = message.readInt(12);

                SequencerActor sequencer = new SequencerActor(sequencerID, x, y, "sequencer-grey-w-panel-white.png", 16, 32, currentScreen.getStage(), currentScreen);

                currentScreen.getTimeDispatcher().addListener(sequencer);
                currentScreen.getStage().addActor(sequencer);
            } else if (param == 1) {
                //update existing sequencer position

                String sequencerID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                SequencerActor sequencer = currentScreen.getSequencerByID(sequencerID);

                if (sequencer != null) { sequencer.setPosition(x, y); }

            } else if (param == 2) {
                //update existing sequencer steps

                String sequencerID = message.readStr(8);

                boolean[] steps = message.readBoolArr(16, 16);

                SequencerActor sequencer = currentScreen.getSequencerByID(sequencerID);

                if (sequencer != null) { sequencer.setSteps(steps); }

            } else if (param == 3) {
                //create sequencer link

                String sequencerID = message.readStr(8);
                String nodeID = message.readStr(8, 32);

                SequencerActor sequencer = currentScreen.getSequencerByID(sequencerID);
                NodeActor node = currentScreen.getNodeByID(nodeID);

                if ((sequencer != null) && (node != null)) { sequencer.setNode(node); }

            } else if (param == 44) {
                //sequencer not found
            } else if (param == 40) {
                //user requested an invalid param
            } else {
                //server sent an invalid param
            }
        } else if (action == 40) {
            //user requested an invalid action
        } else {
            //server sent an invalid action
        }
    }
}
