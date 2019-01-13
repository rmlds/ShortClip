package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class JoinRoomListener extends ClickListener {

    private InitHandler initHandler;
    private TextField textField;
    private Label errorLabel;

    public JoinRoomListener(InitHandler initHandler, TextField textField, Label errorLabel) {
        this.initHandler = initHandler;
        this.textField = textField;
        this.errorLabel = errorLabel;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        System.out.println("Join room clicked. Sending message...");

        try {
            String roomID = textField.getText();

            if (roomID.length() == 8) {
                System.out.println("Join room ID: " + roomID);

                NetworkMessage message = new NetworkMessage();
                message.build(0, 1, 8);
                message.writeStr(roomID);

                initHandler.writeMessage(message);
            } else {
                errorLabel.setText("Error: Room ID should be 8 characters long.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
