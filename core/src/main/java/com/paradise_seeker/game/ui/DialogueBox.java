package com.paradise_seeker.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.List;

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

    public void render(SpriteBatch batch, float fontScale) {
        if (!visible) return;

        batch.draw(background, x, y, width, height);
        font.setColor(Color.WHITE);

        // Save old scale, set new
        float oldScaleX = font.getData().scaleX;
        float oldScaleY = font.getData().scaleY;
        font.getData().setScale(fontScale);

        float textX = x + 20;
        float textY = y + height - 25 * fontScale;
        float lineHeight = font.getLineHeight();
        float maxWidth = (width - 40) / fontScale; // wrap at correct width

        String[] lines = wrapText(text, font, maxWidth);
        GlyphLayout layout = new GlyphLayout();
        for (String line : lines) {
            layout.setText(font, line);
            font.draw(batch, layout, textX, textY);
            textY -= lineHeight + 4 * fontScale;
        }

        // Restore previous font scale
        font.getData().setScale(oldScaleX, oldScaleY);
    }

    // Optionally keep this for backward compatibility (calls with default scale 1.0f)
    public void render(SpriteBatch batch) {
        render(batch, 1.0f);
    }

    private String[] wrapText(String text, BitmapFont font, float maxWidth) {
        GlyphLayout layout = new GlyphLayout();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        List<String> lines = new ArrayList<>();

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
