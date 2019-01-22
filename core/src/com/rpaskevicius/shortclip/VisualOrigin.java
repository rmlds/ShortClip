package com.rpaskevicius.shortclip;

import com.badlogic.gdx.math.Vector2;

public interface VisualOrigin {

    public boolean hasReference();
    public VisualTarget getReference();

    public Vector2 getConnectionPoint();

    public boolean isInitiatingConnection();
    public Vector2 getCursorPosition();

}
