package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetSelector {

    private NodeActor actor;
    private ShortClip screen;

    private List<String> list;
    private ScrollPane scrollPane;

    private boolean enabled;

    public AssetSelector(NodeActor actor, ShortClip screen) {
        this.actor = actor;
        this.screen = screen;

        Skin skin = createSkin();

        list = new List<String>(skin, "list-style");
        list.setItems(AssetMap.nodeSounds);
        list.setSelected("kick-01.wav");
        list.addListener(new AssetSelectorListener(this, screen));

        scrollPane = new ScrollPane(list, skin, "scroll-pane-style");
    }

    public void enable() {
        if (!enabled) {
            enabled = true;
            screen.getCenterUI().add(scrollPane).expand();
        }
    }

    public void disable() {
        if (enabled) {
            enabled = false;
            screen.getCenterUI().removeActor(scrollPane);
        }
    }

    public void updateNodeSound() {
        String sound = list.getSelected();
        actor.setSound(sound);
    }

    public void deliverSelection() {
        NetworkMessage message = new NetworkMessage();

        message.build(0, 2, 9);
        message.writeStr(actor.getID());
        message.writeByte(list.getSelectedIndex(), 8);

        screen.getDataHandler().writeMessage(message);
    }

    public void setSelected(String sound) {
        actor.setSound(sound);
        list.setSelected(sound);
    }

    private Skin createSkin() {
        Skin skin = new Skin();

        skin.add("default", new BitmapFont());

        skin.add("list-selected-texture", new Texture(Gdx.files.internal("list-selected.png")));

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.LIGHT_GRAY;
        listStyle.selection = skin.newDrawable("list-selected-texture", Color.WHITE);
        listStyle.font = skin.getFont("default");
        skin.add("list-style", listStyle);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        skin.add("scroll-pane-style", scrollPaneStyle);

        return skin;
    }

}
