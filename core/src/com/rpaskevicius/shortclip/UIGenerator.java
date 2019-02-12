package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class UIGenerator {

    public static TextButton genVolumeSelector() {
        Skin skin = new Skin();

        skin.add("default", new BitmapFont());
        skin.add("volume-selector-texture", new Texture(Gdx.files.internal("ui-volume-selector.png")));

        TextButton.TextButtonStyle volumeSelectorStyle = new TextButton.TextButtonStyle();
        volumeSelectorStyle.up = skin.newDrawable("volume-selector-texture", Color.WHITE);
        volumeSelectorStyle.down = skin.newDrawable("volume-selector-texture", Color.LIGHT_GRAY);
        volumeSelectorStyle.font = skin.getFont("default");
        skin.add("volume-selector", volumeSelectorStyle);

        return new TextButton(Integer.toString(100), skin, "volume-selector");
    }

}
