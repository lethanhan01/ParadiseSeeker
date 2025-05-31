package com.paradise_seeker.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.Player;

public class HazardObject implements Collidable {
    private Rectangle bounds;
    private Color color;
    private float damagePerSecond;

    public HazardObject(float x, float y, float size, float damagePerSecond, Color color) {
        this.bounds = new Rectangle(x, y, size, size);
        this.damagePerSecond = damagePerSecond;
        this.color = color;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(Player player) {
    	player.takeDamage((int)(damagePerSecond * Gdx.graphics.getDeltaTime()));
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(color);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
