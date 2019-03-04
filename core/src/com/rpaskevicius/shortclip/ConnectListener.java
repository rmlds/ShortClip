package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ConnectListener extends ClickListener {

    private MainMenuScreen screen;
    private TextField connectField;

    private boolean connected;

    public ConnectListener(MainMenuScreen screen, TextField connectField) {
        this.screen = screen;
        this.connectField = connectField;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if (!connected) {
            connected = true;

            String IP = connectField.getText();

            System.out.println("Connect clicked. Connecting to " + IP + "...");

            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            Gdx.app.debug("BUTTON", "Connecting...");

            //screen.setSocket(Gdx.net.newClientSocket(Net.Protocol.TCP, "172.22.158.6", 80, null));

            System.out.println("Connected to " + IP);
        } else {
            System.out.println("Already connected.");
        }
    }
}
