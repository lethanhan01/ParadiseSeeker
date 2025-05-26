package com.paradise_seeker.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DialogueUI {
    private Stage stage;
    private Label dialogueLabel;
    private Window window;
    private TextButton closeButton;

    public DialogueUI(Stage stage, Skin skin) {
        this.stage = stage;

        window = new Window("", skin);
        window.setSize(400, 150);
        window.setPosition(100, 50);

        dialogueLabel = new Label("", skin);
        dialogueLabel.setWrap(true);
        window.add(dialogueLabel).width(380).row();

        closeButton = new TextButton("Tiếp tục", skin);
        closeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                window.setVisible(false);
            }
        });
        window.add(closeButton).padTop(10);

        window.setVisible(false);
        stage.addActor(window);
    }

    public void showDialogue(String text) {
        dialogueLabel.setText(text);
        window.setVisible(true);
    }

    public void hide() {
        window.setVisible(false);
    }
}
