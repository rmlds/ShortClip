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

                String nodeID = message.readCore(8);

                System.out.println("New node ID: " + nodeID);

                System.out.println("Debug core: " + message.debugCore(17));

                int x = SerialUtils.arrayToInt(message.getCore(8,4));
                int y = SerialUtils.arrayToInt(message.getCore(12,4));

                System.out.println("Received message to create new node. x: " + x + " y: " + y);

                NodeActor node = new NodeActor(x, y, "node-purple-w-connector.png", "kick-01.wav", currentScreen.getAssetManager(), currentScreen.getCenterUI());
                currentScreen.getStage().addActor(node);

            } else if (param == 1) {
                //update existing node position

            } else if (param == 2) {
                //update existing node sound

            } else if (param == 44) {
                //node not found

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
