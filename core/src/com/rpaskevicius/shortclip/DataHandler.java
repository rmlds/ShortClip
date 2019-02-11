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

                NodeActor node = new NodeActor(nodeID, x, y, "node-purple-w-connector.png", "kick-01.wav", currentScreen.getAssetManager(), currentScreen);
                currentScreen.getStage().addActor(node);
                currentScreen.getStage().addActor(node.getVolumeSelector());

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
                    String soundString = AssetMap.getSoundString((int)sound);
                    node.getNodeGestureListener().getAssetSelector().setSelected(soundString);
                }

            } else if (param == 3) {
                //update existing node volume

                String nodeID = message.readStr(8);

                int volume = (int)message.readByte(17);

                NodeActor node = currentScreen.getNodeByID(nodeID);

                if (node != null) {
                    node.setVolume((float)(volume) / 100.0f);
                    node.getVolumeSelector().setText(Integer.toString(volume));
                }

            } else if (param == 100) {
                //server is sending an already existing node to user

                String nodeID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                byte sound = message.readByte(16);

                int volume = (int)message.readByte(17);

                NodeActor node = new NodeActor(nodeID, x, y, "node-purple-w-connector.png", "kick-01.wav", currentScreen.getAssetManager(), currentScreen);

                node.setPosition(x, y);

                String soundString = AssetMap.getSoundString((int)sound);
                node.getNodeGestureListener().getAssetSelector().setSelected(soundString);

                node.setVolume((float)(volume) / 100.0f);
                node.getVolumeSelector().setText(Integer.toString(volume));

                currentScreen.getStage().addActor(node);
                currentScreen.getStage().addActor(node.getVolumeSelector());

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

                SequencerActor sequencer = new SequencerActor(sequencerID, x, y, "sequencer-grey-w-panel-white.png", 16, 32, currentScreen);

                currentScreen.getTimeDispatcher().addListener(sequencer);
                currentScreen.getStage().addActor(sequencer);
            } else if (param == 1) {
                //update existing sequencer position

                String sequencerID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                SequencerActor sequencer = currentScreen.getSequencerByID(sequencerID);

                if (sequencer != null) {
                    sequencer.setPosition(x, y);
                }

            } else if (param == 2) {
                //update existing sequencer steps

                String sequencerID = message.readStr(8);

                boolean[] steps = message.readBoolArr(16, 16);

                SequencerActor sequencer = currentScreen.getSequencerByID(sequencerID);

                if (sequencer != null) {
                    sequencer.setSteps(steps);
                }

            } else if (param == 3) {
                //create sequencer link

                String sequencerID = message.readStr(8);
                String nodeID = message.readStr(8, 32);

                SequencerActor sequencer = currentScreen.getSequencerByID(sequencerID);
                NodeActor node = currentScreen.getNodeByID(nodeID);

                if ((sequencer != null) && (node != null)) {
                    sequencer.setNode(node);
                }

            } else if (param == 4) {
                //clear sequencer link

                String sequencerID = message.readStr(8);

                SequencerActor sequencer = currentScreen.getSequencerByID(sequencerID);

                if (sequencer != null) {
                    sequencer.clearNode();
                }

            } else if (param == 100) {
                //server is sending an already existing sequencer to user

                String sequencerID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                boolean[] steps = message.readBoolArr(16, 16);

                SequencerActor sequencer = new SequencerActor(sequencerID, x, y, "sequencer-grey-w-panel-white.png", 16, 32, currentScreen);

                sequencer.setPosition(x, y);

                sequencer.setSteps(steps);

                String nodeID = message.readStr(8, 32);
                NodeActor node = currentScreen.getNodeByID(nodeID);
                sequencer.setNode(node);

                currentScreen.getTimeDispatcher().addListener(sequencer);
                currentScreen.getStage().addActor(sequencer);

            } else if (param == 44) {
                //sequencer not found
            } else if (param == 40) {
                //user requested an invalid param
            } else {
                //server sent an invalid param
            }
        } else if (action == 2) { //something about instruments
            if (param == 0) {
                //create new instrument

                String ID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                InstrumentActor instrument = new InstrumentActor(ID, x, y, currentScreen);
                currentScreen.getStage().addActor(instrument);

            } else if (param == 1) {
                //update existing instrument position

                String ID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                InstrumentActor instrument = currentScreen.getInstrument(ID);

                if (instrument != null) { instrument.setPosition(x, y); }

            } else if (param == 2) {
                //update existing instrument soundset

                String ID = message.readStr(8);

                byte soundset = message.readByte(16);

                InstrumentActor instrument = currentScreen.getInstrument(ID);

                if (instrument != null) {
                    String instrumentString = AssetMap.getInstrumentString((int)soundset);
                    instrument.getListener().getAssetSelector().setSelected(instrumentString);
                }

            } else if (param == 100) {
                //server is sending an already existing instrument to user

                String ID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                byte soundset = message.readByte(16);

                InstrumentActor instrument = new InstrumentActor(ID, x, y, currentScreen);

                String instrumentString = AssetMap.getInstrumentString((int)soundset);
                instrument.getListener().getAssetSelector().setSelected(instrumentString);

                currentScreen.getStage().addActor(instrument);

            } else if (param == 44) {
                //instrument not found
            } else if (param == 40) {
                //user requested an invalid param
            } else {
                //server sent an invalid param
            }

        } else if (action == 3) { //something about piano rolls
            if (param == 0) {
                //create new piano roll

                String ID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                PianoRollActor pianoRoll = new PianoRollActor(ID, x, y, currentScreen);

                currentScreen.getTimeDispatcher().addListener(pianoRoll);
                currentScreen.getStage().addActor(pianoRoll);
                currentScreen.getStage().addActor(pianoRoll.getScaleButton());

            } else if (param == 1) {
                //update existing piano roll position

                String ID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                PianoRollActor pianoRoll = currentScreen.getPianoRoll(ID);

                if (pianoRoll != null) {
                    pianoRoll.setPosition(x, y);
                }

            } else if (param == 2) {
                //set existing piano roll step
                setPianoRollStep(true);

            } else if (param == 3) {
                //clear existing piano roll step
                setPianoRollStep(false);

            } else if (param == 4) {
                //set piano roll link

                String ID = message.readStr(8);
                String link = message.readStr(8, 16);

                PianoRollActor pianoRoll = currentScreen.getPianoRoll(ID);
                InstrumentActor instrument = currentScreen.getInstrument(link);

                if ((pianoRoll != null) && (instrument != null)) {
                    pianoRoll.setInstrument(instrument);
                }

            } else if (param == 5) {
                //clear piano roll link

                String ID = message.readStr(8);

                PianoRollActor pianoRoll = currentScreen.getPianoRoll(ID);

                if (pianoRoll != null) {
                    pianoRoll.clearInstrument();
                }

            } else if (param == 6) {
                //update piano roll scale

                String ID = message.readStr(8);

                byte scale = message.readByte(24);

                PianoRollActor pianoRoll = currentScreen.getPianoRoll(ID);

                if (pianoRoll != null) {
                    String scaleString = ScaleMap.getScaleString((int)scale);
                    pianoRoll.getScaleButtonListener().getScaleSelector().setSelected(scaleString);
                }

            } else if (param == 100) {
                //server is sending existing piano roll

                String ID = message.readStr(8);

                int x = message.readInt(8);
                int y = message.readInt(12);

                String link = message.readStr(8, 16);

                byte scale = message.readByte(24);

                PianoRollActor pianoRoll = new PianoRollActor(ID, x, y, currentScreen);

                InstrumentActor instrument = currentScreen.getInstrument(link);
                pianoRoll.setInstrument(instrument);

                String scaleString = ScaleMap.getScaleString((int)scale);
                pianoRoll.getScaleButtonListener().getScaleSelector().setSelected(scaleString);

                currentScreen.getTimeDispatcher().addListener(pianoRoll);
                currentScreen.getStage().addActor(pianoRoll);
                currentScreen.getStage().addActor(pianoRoll.getScaleButton());

            } else if (param == 101) {
                //server is sending existing piano roll steps

                String ID = message.readStr(8);

                byte stepCount = message.readByte(8);

                PianoRollActor pianoRoll = currentScreen.getPianoRoll(ID);

                if (pianoRoll != null) {
                    for (int i = 0; i < stepCount; i++) {
                        byte absoluteStepIndex = message.readByte(9 + i);

                        int toneIndex = absoluteStepIndex / pianoRoll.getStepCount();
                        int stepIndex = absoluteStepIndex % pianoRoll.getStepCount();

                        pianoRoll.setStepState(toneIndex, stepIndex, true);
                    }
                }

            } else if (param == 44) {
                //sequencer not found
            } else if (param == 40) {
                //user requested an invalid param
            } else {
                //server sent an invalid param
            }

        } else if (action == 10) { // start/stop
            if (param == 0) {
                //start playback

                currentScreen.getTimeDispatcher().start();
                currentScreen.getPlayButton().setChecked(true); //if true, it is playing

            } else if (param == 1) {
                //stop playback

                currentScreen.getTimeDispatcher().stop();
                currentScreen.getPlayButton().setChecked(false); //if false, it is stopped

            } else if (param == 100) {
                //client starts out in stopped state.
                //with this param, server lets us know playback is started on other clients
                //to sync all clients it should be started on this one too.

                //server sends approx playback position, so client is not out of phase with other clients
                int sequencePartial = message.readInt(0);
                System.out.println("Sequence partial: " + sequencePartial);
                currentScreen.getTimeDispatcher().setSequencePartial((long)sequencePartial);

                currentScreen.getTimeDispatcher().start();
                currentScreen.getPlayButton().setChecked(true); //true = playing
            }
        } else if (action == 11) { // bpm
            if (param == 0) {
                //change bpm

                int bpm = message.readInt(0);

                currentScreen.getTimeDispatcher().setBpm((float)bpm);
                currentScreen.getBpmButton().setText((int)currentScreen.getTimeDispatcher().getBpm() + "");
            } else if (param == 100) {
                //client starts out with bpm = 120.
                //with this param server lets us know the bpm has been changed

                //however, the code is identical to param == 0,
                //so server sends param = 0 when a new player joins
            }
        } else if (action == 40) {
            //user requested an invalid action
        } else {
            //server sent an invalid action
        }
    }

    private void setPianoRollStep(boolean state) {
        String ID = message.readStr(8);

        byte index = message.readByte(8);

        PianoRollActor pianoRoll = currentScreen.getPianoRoll(ID);

        int toneIndex = index / pianoRoll.getStepCount();
        int stepIndex = index % pianoRoll.getStepCount();

        //noinspection ConstantConditions
        if (pianoRoll != null) {
            pianoRoll.setStepState(toneIndex, stepIndex, state);
        }
    }
}
