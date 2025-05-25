package com.paradise_seeker.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.Player;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class HUD {
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
    public HUD(Player player) {
        this.player = player;
        this.shapeRenderer = new ShapeRenderer();
        this.spriteBatch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            com.badlogic.gdx.Gdx.files.internal("fonts/MinecraftStandard.otf")
        );
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 10;
        this.font = generator.generateFont(parameter);
        generator.dispose();

        font.setColor(Color.WHITE);
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
     // Load button textures
        inventoryButton = new Texture(Gdx.files.internal("ui/HUD/inventory.png"));
        pauseButton = new Texture(Gdx.files.internal("ui/HUD/pause.png"));
    }
    private float inventoryButtonWidth = 44f;
    private float inventoryButtonHeight = 44f;
    private float pauseButtonWidth = 44f;
    private float pauseButtonHeight = 44f;
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    public void render(float delta) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        // Adjust bar sizes based on screen dimensions
        float scaledBarWidth = screenWidth * 0.45f; // 40% of screen width
        float scaledBarHeight = screenHeight * 0.07f; // 4% of screen height
        float hpPercent = Math.max(0, Math.min(player.hp / (float) Player.MAX_HP, 1f));
        float mpPercent = Math.max(0, Math.min(player.mp / (float) Player.MAX_MP, 1f));

        int frameIndexhp = Math.round((1 - hpPercent) * 73);
        int frameIndexmp = Math.round((1 - mpPercent) * 73);

        spriteBatch.begin();
        spriteBatch.draw(hpBarFrames[frameIndexhp], PADDING, screenHeight - PADDING - scaledBarHeight, scaledBarWidth, scaledBarHeight);
        spriteBatch.draw(mpBarFrames[frameIndexmp], PADDING*0.95f, screenHeight - PADDING - scaledBarHeight * 1.8f, scaledBarWidth, scaledBarHeight);
        
        float baseHeight = 450f; // or 1080f, depending on your base design
        float fontScale = screenHeight / baseHeight; // Relative to a 720p or 1080p baseline
        font.getData().setScale(fontScale);

        if (player.showInteractMessage) {
            // Compute position under MP bar, with dynamic vertical offset
            float messageY = screenHeight - PADDING - (screenHeight * 0.07f) * 2.7f - (screenHeight * 0.03f);
            font.draw(spriteBatch, "> Press F to interact", PADDING, messageY);
        }
        // Adjust button sizes based on screen dimensions
        inventoryButtonWidth = screenWidth * 0.03f *1.5f; // 5% of screen width + 0.5f for scaling
        inventoryButtonHeight = screenHeight * 0.05f*1.5f; // 5% of screen height
        pauseButtonWidth = screenWidth * 0.03f*1.5f; // 5% of screen width
        pauseButtonHeight = screenHeight * 0.05f*1.5f; // 5% of screen height
     // Draw buttons with dynamically adjusted width and height
        spriteBatch.draw(inventoryButton, screenWidth - PADDING - inventoryButtonWidth * 2.2f, screenHeight - PADDING - inventoryButtonHeight, inventoryButtonWidth, inventoryButtonHeight);
        spriteBatch.draw(pauseButton, screenWidth - PADDING - pauseButtonWidth, screenHeight - PADDING - pauseButtonHeight, pauseButtonWidth, pauseButtonHeight);
        spriteBatch.end();
    }


    public void dispose() {
    	shapeRenderer.dispose();
        for (Texture texture : hpBarFrames) {
            texture.dispose();
        }
        for (Texture texture : mpBarFrames) {
            texture.dispose();
        }
        spriteBatch.dispose();
        font.dispose();
        // Dispose button textures
        inventoryButton.dispose();
        pauseButton.dispose();
    }
}
