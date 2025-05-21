package com.paradise_seeker.game.entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.Player;

public class MPitem implements Collidable {
    private Rectangle bounds;
    private Texture texture;
    private int manaAmount;
    private boolean active = true;

    public MPitem(float x, float y, float size, String texturePath, int manaAmount) {
        this.bounds = new Rectangle(x, y, size, size);
        this.manaAmount = manaAmount;
        this.texture = new Texture(texturePath);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(Player player) {
        if (active) {
            player.mp = Math.min(Player.MAX_MP, player.mp + manaAmount);
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
