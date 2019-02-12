package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public interface VolumeActor {

    public String getID(); //implemented in NetworkedActor

    public int getVolume();
    public void setVolume(int volume);

    public TextButton getVolumeSelector();
    public VolumeSelectorListener getVolumeSelectorListener();

}
