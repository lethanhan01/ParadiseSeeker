package com.paradise_seeker.game.entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.Player;

public class ATKitem implements Collidable {
    private Rectangle bounds;
    private Texture texture;
    private int atkBoost;
    private boolean active = true;

    public ATKitem(float x, float y, float size, String texturePath, int atkBoost) {
        this.bounds = new Rectangle(x, y, size, size);
        this.atkBoost = atkBoost;
        this.texture = new Texture(texturePath);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(Player player) {
        if (active) {
            player.atk += atkBoost; // tăng chỉ số tấn công
            active = false; // vật phẩm biến mất sau khi nhặt
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
