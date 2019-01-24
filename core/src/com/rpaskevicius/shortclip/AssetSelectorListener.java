package com.rpaskevicius.shortclip;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AssetSelectorListener extends ClickListener {

    private AssetSelector selector;

    public AssetSelectorListener(AssetSelector selector) {
        this.selector = selector;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        selector.disable();
        selector.updateHost();
        selector.deliverSelection();
    }

}
