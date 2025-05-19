package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class LaserBeam {
    private float x, y;
    private float speed = 8f;
    private int damage;
    private boolean active = true;
    private Rectangle hitbox;
    private String direction;

    public LaserBeam(float x, float y, int damage, String direction) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.direction = direction;
        this.hitbox = new Rectangle(x, y, 32, 16); // Kích thước tạm thời
    }

    public void update() {
        switch (direction) {
            case "up": y += speed; break;
            case "down": y -= speed; break;
            case "left": x -= speed; break;
            case "right": x += speed; break;
        }
        hitbox.setPosition(x, y);

        if (x < 0 || x > 512 || y < 0 || y > 512) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        // Tạm thời để trống hoặc bạn có thể thêm batch.draw(...)
        // Ví dụ:
        // batch.draw(laserTexture, x, y, hitbox.width, hitbox.height);
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getDamage() {
        return damage;
    }
} 
