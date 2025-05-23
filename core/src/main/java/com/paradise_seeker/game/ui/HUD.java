package com.paradise_seeker.game.ui;

import com.badlogic.gdx.graphics.Color;
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
    private static final float BAR_WIDTH = 200f;
    private static final float BAR_HEIGHT = 20f;
    private static final float PADDING = 10f;
    private static final float SPACING = 5f;

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
    }

    public void render(float screenHeight) {
        float hpPercent = Math.max(0, Math.min(player.hp / (float) Player.MAX_HP, 1f));
        float mpPercent = Math.max(0, Math.min(player.mp / (float) Player.MAX_MP, 1f));

        // Draw HP Bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(PADDING, screenHeight - PADDING - BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(PADDING, screenHeight - PADDING - BAR_HEIGHT, BAR_WIDTH * hpPercent, BAR_HEIGHT);
        shapeRenderer.end();

        // Draw MP Bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(PADDING, screenHeight - PADDING - BAR_HEIGHT * 2 - SPACING, BAR_WIDTH, BAR_HEIGHT);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(PADDING, screenHeight - PADDING - BAR_HEIGHT * 2 - SPACING, BAR_WIDTH * mpPercent, BAR_HEIGHT);
        shapeRenderer.end();

        // Draw borders
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(PADDING, screenHeight - PADDING - BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT); // HP border
        shapeRenderer.rect(PADDING, screenHeight - PADDING - BAR_HEIGHT * 2 - SPACING, BAR_WIDTH, BAR_HEIGHT); // MP border
        shapeRenderer.end();

        // Draw text
        spriteBatch.begin();
        font.draw(spriteBatch, "HP: " + player.hp + " / " + Player.MAX_HP,
                  PADDING + 5, screenHeight - PADDING - 5);
        font.draw(spriteBatch, "MP: " + player.mp + " / " + Player.MAX_MP,
                  PADDING + 5, screenHeight - PADDING - BAR_HEIGHT - SPACING - 5);
        spriteBatch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        font.dispose();
    }
}
