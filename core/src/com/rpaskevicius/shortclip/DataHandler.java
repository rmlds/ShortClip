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

    }
}
