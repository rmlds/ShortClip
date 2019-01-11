package com.rpaskevicius.shortclip;

import com.badlogic.gdx.net.Socket;

import java.io.IOException;

public class NetworkHandler {

    private Socket socket;

    private NetworkMessage message = new NetworkMessage();

    private boolean receivedHeader = false;

    public NetworkHandler(Socket socket) {
        this.socket = socket;
    }

    public void readMessage() {
        if (receivedHeader) {
            readData();
        } else {
            readHeader();
        }
    }

    public void readHeader() {
        try {
            if (socket.getInputStream().available() >= 2) {
                socket.getInputStream().read(message.getHeader(), 0, 2);

                message.decodeHeader();

                System.out.println("Received header: " + message.getHeader()[0] + " " + message.getHeader()[1]);

                receivedHeader = true;

                readData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readData() {
        try {
            if (socket.getInputStream().available() >= message.getLength()) {
                socket.getInputStream().read(message.getData(), 0, message.getLength());

                System.out.println("Received data: " + message.getAction() + " " + message.getParam() + " core: " + message.readCore(message.getLength() - 2));

                //TODO handle the message
                byte action = message.getAction();
                byte param = message.getParam();

                if (action == 0) { //something about rooms
                    if (param == 0) {
                        //new room created successfully
                        //TODO switch to game screen

                        String roomID = message.readCore(8);
                        System.out.println("Room success. Room ID: " + roomID);

                        //launchScreen.setScreen(new ShortClip(launchScreen, assetManager));

                    } else if (param == 1) {
                        //existing room joined successfully
                        //TODO switch to game screen

                        String roomID = message.readCore(8);
                        System.out.println("Room success. Room ID: " + roomID);

                        //launchScreen.setScreen(new ShortClip(launchScreen, assetManager));

                    } else if (param == 44) {
                        //room not found
                        //TODO display helpful message

                        System.out.println("Room error. Room not found.");

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

                receivedHeader = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMessage(NetworkMessage message) {
        try {
            System.out.println("Sending header...");
            socket.getOutputStream().write(message.getHeader(), 0, 2);

            System.out.println("Sending data...");
            socket.getOutputStream().write(message.getData(), 0, message.getLength());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
