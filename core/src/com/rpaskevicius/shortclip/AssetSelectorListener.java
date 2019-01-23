package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AssetSelectorListener extends ClickListener {

    private AssetSelector selector;
    private ShortClip screen;

    public AssetSelectorListener(AssetSelector selector, ShortClip screen) {
        this.selector = selector;
        this.screen = screen;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        selector.disable();
        selector.updateNodeSound();
        selector.deliverSelection();
    }

}
