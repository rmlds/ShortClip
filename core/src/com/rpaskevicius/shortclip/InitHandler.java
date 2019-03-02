package com.rpaskevicius.shortclip;

import com.badlogic.gdx.net.Socket;

public class InitHandler extends NetworkHandler {

    private MainMenuScreen currentScreen;

    public InitHandler(Socket socket, MainMenuScreen currentScreen) {
        super(socket);
        this.currentScreen = currentScreen;
    }

    @Override
    protected void handleMessage() {
        byte action = message.getAction();
        byte param = message.getParam();

        if (action == 0) { //something about rooms
            if (param == 0) {
                //new room created successfully

                String roomID = message.readStr(8);
                System.out.println("Room success. Room ID: " + roomID);

                launchShortClip(roomID);

            } else if (param == 1) {
                //existing room joined successfully

                String roomID = message.readStr(8);
                System.out.println("Room success. Room ID: " + roomID);

                launchShortClip(roomID);

            } else if (param == 44) {
                //room not found
                currentScreen.getErrorLabel().setText("Error: Room not found.");
            } else if (param == 40) {
                //user requested an invalid param
                currentScreen.getErrorLabel().setText("Error: user requested an invalid param.");
            } else {
                //server sent an invalid param
                currentScreen.getErrorLabel().setText("Error: server sent an invalid param.");
            }
        } else if (action == 40) {
            //user requested an invalid action
            currentScreen.getErrorLabel().setText("Error: user requested an invalid action.");
        } else {
            //server sent an invalid action
            currentScreen.getErrorLabel().setText("Error: server sent an invalid action.");
        }
    }

    private void launchShortClip(String roomID) {
        currentScreen.getLaunchScreen().setScreen(
                new ShortClip(currentScreen.getLaunchScreen(), currentScreen.getAssetManager(), socket, roomID)
        );
    }

    public void setSocket(Socket socket) {
        super.socket = socket;
    }
}
