package com.paradise_seeker.game.entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.Player;

public abstract class GameObject implements Collidable {
    protected Rectangle bounds;
    protected Texture texture;

    public GameObject(float x, float y, float width, float height, String texturePath) {
        this.bounds = new Rectangle(x, y, width, height);
        this.texture = new Texture(texturePath);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public abstract void onPlayerCollision(Player player);

    @Override
    public void onCollision(Player player) {
        onPlayerCollision(player);
    }

    public void dispose() {
        texture.dispose();
    }
}
