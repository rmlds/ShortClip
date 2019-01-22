package com.rpaskevicius.shortclip;

public class NetworkedDuoplexedActor extends NetworkedActor {

    private float bound;

    public NetworkedDuoplexedActor(String ID, float bound) {
        super(ID);
        this.bound = bound;
    }

    public boolean insideBound(float x) {
        return (x < bound);
    }

    public float getBound() {
        return bound;
    }

    public void setBound(float bound) {
        this.bound = bound;
    }

}
