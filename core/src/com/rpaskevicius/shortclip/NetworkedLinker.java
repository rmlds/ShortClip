package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class NetworkedLinker extends NetworkedDuoplexedPan {

    private NetworkedDuoplexedActor actor;

    private Vector2 cursorPosition = new Vector2(0, 0);

    private boolean initiatingConnection;

    public NetworkedLinker(NetworkedDuoplexedActor actor, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;
    }

    public Actor getHitResult(float x, float y) {
        Vector2 hitPoint = actor.localToStageCoordinates(new Vector2(x, y));
        return actor.getStage().hit(hitPoint.x, hitPoint.y, false);
    }

    public void deliverReference(NetworkedActor target, int param) {
        NetworkMessage message = new NetworkMessage();

        message.build(NetworkMap.getCode(actor), param, 16);
        message.writeStr(actor.getID());
        message.writeStr(target.getID(), 8);

        screen.getDataHandler().writeMessage(message);
    }

    public void deliverClear(int param) {
        NetworkMessage message = new NetworkMessage();

        message.build(NetworkMap.getCode(actor), param, 8);
        message.writeStr(actor.getID());

        screen.getDataHandler().writeMessage(message);
    }

    @Override
    public void secondaryPanHandler(InputEvent event, float x, float y, float deltaX, float deltaY) {
        updateCursorPosition(x, y);
    }

    @Override
    public void secondaryTouchDownHandler(InputEvent event, float x, float y, int pointer, int button) {
        initiatingConnection = true;
        updateCursorPosition(x, y);
    }

    public void updateCursorPosition(float x, float y) {
        cursorPosition = actor.localToStageCoordinates(new Vector2(x, y));
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (initiatingConnection) {
            Actor hitResult = getHitResult(x, y);

            linkHandler(hitResult);

            initiatingConnection = false;
        }
    }

    public void linkHandler(Actor hitResult) {}

    public Vector2 getCursorPosition() {
        return cursorPosition;
    }

    public boolean isInitiatingConnection() {
        return initiatingConnection;
    }

}
