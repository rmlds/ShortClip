package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class NetworkedDuoplexedPan extends NetworkedPan {

    private NetworkedDuoplexedActor actor;

    private boolean touchDownInBounds;

    public NetworkedDuoplexedPan(NetworkedDuoplexedActor actor, ShortClip screen) {
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
            secondaryTouchDownHandler(event, x, y, pointer, button);
        }
    }

    public void secondaryPanHandler(InputEvent event, float x, float y, float deltaX, float deltaY) {}
    public void secondaryTouchDownHandler(InputEvent event, float x, float y, int pointer, int button) {}

}
