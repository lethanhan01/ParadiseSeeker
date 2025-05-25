package com.paradise_seeker.game.entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.Player;

public class Skill2item implements Collidable {
    private Rectangle bounds;
    private Texture texture;
    private boolean active = true;

    public Skill2item(float x, float y, float size, String texturePath) {
        this.bounds = new Rectangle(x, y, size, size);
        this.texture = new Texture(texturePath);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(Player player) {
        if (active) {
            player.playerSkill2.setDamageMultiplier(2.0f); // Nhân 2 damage skill2
            active = false;
            System.out.println("Skill2 damage doubled!");
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
