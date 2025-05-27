package com.paradise_seeker.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.Player;

public class HUD {
    private String notificationMessage = "";
    private float notificationTimer = 0f;
    private static final float NOTIFICATION_DISPLAY_TIME = 2.2f; // seconds

    public ShapeRenderer shapeRenderer;
    public SpriteBatch spriteBatch;
    private BitmapFont font;
    private Player player;
    private Texture[] hpBarFrames;
    private Texture[] mpBarFrames;
    private static final float BAR_WIDTH = 200f;
    private static final float BAR_HEIGHT = 20f;
    private static final float PADDING = 10f;
    private static final float SPACING = 5f;
    private Texture inventoryButton;
    private Texture pauseButton;

    private float inventoryButtonWidth = 44f;
    private float inventoryButtonHeight = 44f;
    private float pauseButtonWidth = 44f;
    private float pauseButtonHeight = 44f;

    public HUD(Player player, BitmapFont font) {
        this.player = player;
        this.font = font;
        this.shapeRenderer = new ShapeRenderer();
        this.spriteBatch = new SpriteBatch();

        hpBarFrames = new Texture[74];
        for (int i = 0; i < 74; i++) {
            String filename = String.format("ui/HUD/hp_bar_fg/hpbar/hpbar%02d.png", i);
            hpBarFrames[i] = new Texture(Gdx.files.internal(filename));
        }
        mpBarFrames = new Texture[74];
        for (int i = 0; i < 74; i++) {
            String filename = String.format("ui/HUD/mp_bar_fg/mpbar/mpbar%02d.png", i);
            mpBarFrames[i] = new Texture(Gdx.files.internal(filename));
        }
        inventoryButton = new Texture(Gdx.files.internal("ui/HUD/inventory.png"));
        pauseButton = new Texture(Gdx.files.internal("ui/HUD/pause.png"));
    }

    public void showNotification(String message) {
        this.notificationMessage = message;
        this.notificationTimer = NOTIFICATION_DISPLAY_TIME;
    }

    public void render(float delta) {
    	
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float scaledBarWidth = screenWidth * 0.45f;
        float scaledBarHeight = screenHeight * 0.07f;
        float hpPercent = Math.max(0, Math.min(player.hp / (float) Player.MAX_HP, 1f));
        float mpPercent = Math.max(0, Math.min(player.mp / (float) Player.MAX_MP, 1f));

        int frameIndexhp = Math.round((1 - hpPercent) * 73);
        int frameIndexmp = Math.round((1 - mpPercent) * 73);

        spriteBatch.begin();

        spriteBatch.draw(hpBarFrames[frameIndexhp], PADDING, screenHeight - PADDING - scaledBarHeight, scaledBarWidth, scaledBarHeight);
        spriteBatch.draw(mpBarFrames[frameIndexmp], PADDING * 0.95f, screenHeight - PADDING - scaledBarHeight * 1.8f, scaledBarWidth, scaledBarHeight);

        float baseHeight = 570f;
        float fontScale = screenHeight / baseHeight;

        // -- Save old scale, set new scale (because font is shared!) --
        float oldScaleX = font.getData().scaleX;
        float oldScaleY = font.getData().scaleY;
        font.getData().setScale(fontScale);

        // Show notification below MP bar
        if (font != null && spriteBatch != null && notificationMessage != null) {
            if (notificationTimer > 0f && !notificationMessage.isEmpty()) {
                float notificationY = screenHeight - PADDING - (scaledBarHeight * 2.7f) - (screenHeight * 0.07f);
                font.draw(spriteBatch, notificationMessage, PADDING, notificationY);
                notificationTimer -= delta * 0.00005f;
                if (notificationTimer <= 0f) {
                    notificationMessage = "";
                    notificationTimer = 0f;
                }
            }
        }

        // Show interact message lower than notification
        if (player.showInteractMessage) {
            float messageY = screenHeight - PADDING - (scaledBarHeight * 2.7f) - (screenHeight * 0.17f);
            font.draw(spriteBatch, "> Press F to interact", PADDING, messageY);
        }

        // Restore previous font scale!
        font.getData().setScale(oldScaleX, oldScaleY);

        // Button rendering (unchanged)
        inventoryButtonWidth = screenWidth * 0.03f * 1.5f;
        inventoryButtonHeight = screenHeight * 0.05f * 1.5f;
        pauseButtonWidth = screenWidth * 0.03f * 1.5f;
        pauseButtonHeight = screenHeight * 0.05f * 1.5f;

        spriteBatch.draw(inventoryButton, screenWidth - PADDING - inventoryButtonWidth * 2.2f, screenHeight - PADDING - inventoryButtonHeight, inventoryButtonWidth, inventoryButtonHeight);
        spriteBatch.draw(pauseButton, screenWidth - PADDING - pauseButtonWidth, screenHeight - PADDING - pauseButtonHeight, pauseButtonWidth, pauseButtonHeight);
        spriteBatch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        for (Texture texture : hpBarFrames) texture.dispose();
        for (Texture texture : mpBarFrames) texture.dispose();
        spriteBatch.dispose();
        // Do not dispose font (dispose in Main if you own it there)
        inventoryButton.dispose();
        pauseButton.dispose();
    }
}
