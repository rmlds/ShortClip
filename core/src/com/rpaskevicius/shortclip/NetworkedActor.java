package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class NetworkedActor extends Actor {

    private String ID;

    public NetworkedActor(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
