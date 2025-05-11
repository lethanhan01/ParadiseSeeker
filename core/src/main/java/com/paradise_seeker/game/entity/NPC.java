package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class NPC implements Renderable, Collidable, Interactable {
    private Polygon shape;
    private Rectangle bounds;
    private Texture texture;

    public NPC(float x, float y, float size, Texture texture) {
        this.texture = texture;

        float half = size / 2f;
        float[] vertices = new float[] {
            x, y,
            x + half, y + size,
            x + size, y
        };

        shape = new Polygon(vertices);
        bounds = shape.getBoundingRectangle();
    }

    @Override
    public void render(SpriteBatch batch) {
        // Vẽ texture đơn giản theo bounding box (thay vì polygon nếu không cần custom shape)
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(Collidable other) {
    }

    @Override
    public void interact(Player player) {
        System.out.println("Xin chào! Tôi là NPC.");
    }
}
