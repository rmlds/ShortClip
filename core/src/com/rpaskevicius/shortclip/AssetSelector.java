package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetSelector {

    private NetworkedActor actor;
    protected ShortClip screen;

    private List<String> list;
    private ScrollPane scrollPane;

    private boolean enabled;

    public AssetSelector(NetworkedActor actor, ShortClip screen) {
        this.actor = actor;
        this.screen = screen;

        Skin skin = createSkin();

        list = new List<String>(skin, "list-style");
        list.addListener(new AssetSelectorListener(this));

        scrollPane = new ScrollPane(list, skin, "scroll-pane-style");
    }

    public void setup(String[] items, String selected) {
        list.setItems(items);
        list.setSelected(selected);
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

    public void deliverSelection() { deliverSelection(2); }

    public void deliverSelection(int param) {
        NetworkMessage message = new NetworkMessage();

        message.build(NetworkMap.getCode(this), param, 9);
        message.writeStr(actor.getID());
        message.writeByte(list.getSelectedIndex(), 8);

        screen.getDataHandler().writeMessage(message);
    }

    public void setSelected(String sound) {
        updateHost(sound);
        list.setSelected(sound);
    }

    public void updateHost() { updateHost(list.getSelected()); }
    public void updateHost(String selection) {}

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
