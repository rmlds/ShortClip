package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class NetworkedMultiplexedPan extends NetworkedPan {

    private NetworkedMultiplexedActor actor;

    private boolean touchDownInBounds;

    public NetworkedMultiplexedPan(NetworkedMultiplexedActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;
    }

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {

        if (touchDownInBounds) {
            offsetPosition(deltaX, deltaY);
        } else {
            secondaryPanHandler(event, x, y, deltaX, deltaY);
        }

        event.handle(); //inform stage that this event has been handled
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (actor.insideBound(x)) {
            touchDownInBounds = true;
        } else {
            touchDownInBounds = false;
        }

        touchDownHandler(event, x, y, pointer, button);
    }

    public void secondaryPanHandler(InputEvent event, float x, float y, float deltaX, float deltaY) {}
    public void touchDownHandler(InputEvent event, float x, float y, int pointer, int button) {}

}
