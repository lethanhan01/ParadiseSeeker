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
    private String name;
    private String dialogue;

    public NPC(float x, float y, float size, Texture texture, String name, String dialogue) {
        this.texture = texture;
        this.name = name;
        this.dialogue = dialogue;

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
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    public void onCollision(Collidable other) {
        //
    }

    @Override
    public void onCollision(Player player) {
        // NPC không gây sát thương cho người chơi
    }

    public void interact(Player player) {
        System.out.println("Xin chào! Tôi là " + name + ". " + dialogue);
    }
}
