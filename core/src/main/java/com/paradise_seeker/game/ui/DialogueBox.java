package com.paradise_seeker.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class DialogueBox {
    private Texture background;
    private BitmapFont font;
    private String text;
    private boolean visible;
    private float x, y, width, height;

    public DialogueBox(String text, Texture background, BitmapFont font, float x, float y, float width, float height) {
        this.text = text;
        this.background = background;
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = false;
    }

    public void show(String newText) {
        this.text = newText;
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;

        // Vẽ khung nền hộp thoại
        batch.draw(background, x, y, width, height);

        // Vẽ văn bản nhiều dòng
        font.setColor(Color.WHITE);
        GlyphLayout layout = new GlyphLayout();
        float textX = x + 20;
        float textY = y + height - 25;
        float lineHeight = font.getLineHeight();
        float maxWidth = width - 40;

        String[] lines = wrapText(text, font, maxWidth);
        for (String line : lines) {
            layout.setText(font, line);
            font.draw(batch, layout, textX, textY);
            textY -= lineHeight + 4;
        }
    }

    private String[] wrapText(String text, BitmapFont font, float maxWidth) {
        GlyphLayout layout = new GlyphLayout();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        java.util.List<String> lines = new java.util.ArrayList<>();

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            layout.setText(font, testLine);
            if (layout.width > maxWidth) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        lines.add(line.toString());
        return lines.toArray(new String[0]);
    }
}
