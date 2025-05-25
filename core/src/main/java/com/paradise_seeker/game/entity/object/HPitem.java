package com.paradise_seeker.game.entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.Player;

public class HPitem implements Collidable {
    private Rectangle bounds;
    private Texture texture;
    private int healAmount;
    private boolean active = true;

    public HPitem(float x, float y, float size, String texturePath, int healAmount) {
        this.bounds = new Rectangle(x, y, size, size);
        this.healAmount = healAmount;
        this.texture = new Texture(texturePath);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(Player player) {
        if (active) {
            player.hp = Math.min(Player.MAX_HP, player.hp + healAmount);
            active = false;
            System.out.println("Player healed for " + healAmount + " HP.");
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
