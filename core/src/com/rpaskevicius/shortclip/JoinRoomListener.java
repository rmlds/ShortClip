package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class JoinRoomListener extends ClickListener {

    private InitHandler initHandler;
    private TextField textField;

    public JoinRoomListener(InitHandler initHandler, TextField textField) {
        this.initHandler = initHandler;
        this.textField = textField;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        System.out.println("Join room clicked. Sending message...");

        try {
            byte[] roomID = textField.getText().getBytes("US-ASCII");

            if (roomID.length != 8) {
                System.out.println(roomID.length);
                throw new Exception("Room ID should be 8 characters long."); //TODO display helpful text to the user
            }

            for (byte singleByte : roomID) {
                System.out.print(singleByte + " ");
            }
            System.out.println("");

            NetworkMessage message = new NetworkMessage();
            message.build(0, 1, 8);
            message.writeCore(roomID);

            initHandler.writeMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
