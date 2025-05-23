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
            String filename = String.format("ui/HUD/hp_bar_fg/hpbar/hp_bar%02d.png", i);
            hpBarFrames[i] = new Texture(Gdx.files.internal(filename));
        }
        
        mpBarFrames = new Texture[74];
        for (int i = 0; i < 74; i++) {
			String filename = String.format("ui/HUD/mp_bar_fg/mpbar/mp_bar%02d.png", i);
			mpBarFrames[i] = new Texture(Gdx.files.internal(filename));
	}
    }

    public void render(float screenHeight) {
    	float hpPercent = Math.max(0, Math.min(player.hp / (float) Player.MAX_HP, 1f));
        float mpPercent = Math.max(0, Math.min(player.mp / (float) Player.MAX_MP, 1f));

        int frameIndexhp = Math.round((1 - hpPercent) * 73);
        int frameIndexmp = Math.round((1 - mpPercent) * 73);

        spriteBatch.begin();
        spriteBatch.draw(hpBarFrames[frameIndexhp], PADDING, screenHeight - PADDING - BAR_HEIGHT);
        spriteBatch.draw(mpBarFrames[frameIndexmp], PADDING, screenHeight - PADDING - BAR_HEIGHT*2);
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
    }
}
