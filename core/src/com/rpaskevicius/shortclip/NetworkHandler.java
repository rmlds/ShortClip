package com.rpaskevicius.shortclip;

import com.badlogic.gdx.net.Socket;

import java.io.IOException;

public abstract class NetworkHandler {

    protected Socket socket;

    protected NetworkMessage message = new NetworkMessage();

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

    private void readHeader() {
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

    private void readData() {
        try {
            if (socket.getInputStream().available() >= message.getLength()) {
                socket.getInputStream().read(message.getData(), 0, message.getLength());

                System.out.println("Received data: " + message.getAction() + " " + message.getParam() + " debug: " + message.debug(message.getLength() - 2));

                handleMessage();

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

    protected abstract void handleMessage();
}
