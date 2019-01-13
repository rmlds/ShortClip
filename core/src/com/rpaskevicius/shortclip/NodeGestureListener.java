package com.rpaskevicius.shortclip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NodeGestureListener extends ActorGestureListener {

    private NodeActor nodeActor;

    private AssetManager assetManager;
    private String[] sounds;

    private List<String> list;
    private ScrollPane scrollPane;
    private final Table centerUI;
    private boolean selectorEnabled;

    private ShortClip currentScreen;

    public NodeGestureListener(final NodeActor nodeActor, AssetManager assetManager, Table centerUI, ShortClip currentScreen) {
        this.nodeActor = nodeActor;
        this.assetManager = assetManager;
        this.centerUI = centerUI;
        this.currentScreen = currentScreen;

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

        list.addListener(new AssetListListener(list, this.nodeActor, this.centerUI, scrollPane, this)); //TODO wtf is this constructor

    }

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {

        float newX = nodeActor.getX() + deltaX;
        float newY = nodeActor.getY() + deltaY;

        //Move the node
        nodeActor.setPosition(newX, newY);
        System.out.println("Node: " + nodeActor.getX() + " " + nodeActor.getY());

        //TODO send the new position to the server
        NetworkMessage message = new NetworkMessage();
        message.build(0, 1, 16);
        message.writeID(nodeActor.getNodeID());
        message.writeCore(SerialUtils.intToArray((int)newX), 8);
        message.writeCore(SerialUtils.intToArray((int)newY), 12);

        System.out.println("Sending message to update node position. Core: ");
        System.out.println(message.debugCore(16));

        currentScreen.getDataHandler().writeMessage(message);

        //Moving the node also updates lineEnd position
        if (nodeActor.hasSequencer() && nodeActor.getSequencer().hasLine()) {

            Vector2 lineEnd = nodeActor.getConnectionPoint();

            nodeActor.getSequencer().getLine().setEnd(lineEnd);
        }

        event.handle(); //inform Stage that this event has been handled
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        System.out.println("Tap.");

        //TODO open UI to choose a different sound

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
}
