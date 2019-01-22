package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class NodeGestureListener extends NetworkedPan {

    private NodeActor actor;

    private AssetManager assetManager;
    private String[] sounds;

    private List<String> list;
    private ScrollPane scrollPane;
    private final Table centerUI;
    private boolean selectorEnabled;

    public NodeGestureListener(NodeActor actor, AssetManager assetManager, Table centerUI, ShortClip screen) {
        super(actor, screen);
        this.actor = actor;
        this.assetManager = assetManager;
        this.centerUI = centerUI;

        //sounds should be the same as in AssetLoadingScreen //TODO should be static
        sounds = new String[] {
                "closed-hihat-01.wav",
                "closed-hihat-02.wav",
                "kick-01.wav",
                "kick-02.wav",
                "kick-03.wav",
                "open-hihat-01.wav",
                "shaker-01.wav",
                "shaker-02.wav",
                "shaker-03.wav",
                "snare-01.wav",
                "snare-02.wav",
                "tambourine-01.wav"
        };

        Skin skin = createSkin();

        list = new List<String>(skin, "not-default");
        list.setItems(sounds);
        list.setSelected("kick-01.wav");

        scrollPane = new ScrollPane(list, skin);

        list.addListener(new AssetListListener(this.screen, list, this.actor, this.centerUI, scrollPane, this)); //TODO wtf is this constructor

    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {

        //open UI to choose a different sound
        if (!selectorEnabled) {
            selectorEnabled = true;

            centerUI.add(scrollPane).expand();
        } else {
            System.out.println("Selector already enabled.");
        }

    }

    private Skin createSkin() {
        Skin skin = new Skin();

        skin.add("default", new BitmapFont());

        skin.add("list-selected-texture", new Texture(Gdx.files.internal("list-selected.png")));

        ListStyle listStyle = new ListStyle();
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.LIGHT_GRAY;
        listStyle.selection = skin.newDrawable("list-selected-texture", Color.WHITE);
        listStyle.font = skin.getFont("default");
        skin.add("not-default", listStyle);

        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        skin.add("default", scrollPaneStyle);

        return skin;
    }

    public void setSelectorEnabled(boolean state) {
        this.selectorEnabled = state;
    }

    public List<String> getList() {
        return list;
    }
}
