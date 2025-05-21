package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LaserBeam {
    private float x, y;
    private float speed = 10f; // Tăng tốc độ cho đạn bay nhanh hơn
    private int damage;
    private boolean active = true;
    private Rectangle hitbox;
    private String direction;
    // Biên map (có thể chỉnh lại cho đúng map bạn)
    private static final float MIN_X = 0f;
    private static final float MAX_X = 100f;
    private static final float MIN_Y = 0f;
    private static final float MAX_Y = 100f;

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private String directionRaw; // Lưu hướng gốc
    private Animation<TextureRegion> animDown; // Animation hướng down để fallback

    public LaserBeam(float x, float y, int damage, String direction, Animation<TextureRegion> animation) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.direction = direction;
        this.hitbox = new Rectangle(x, y, 1f, 1f); // Kích thước tạm thời, chỉnh theo sprite nếu cần
        this.animation = animation;
        this.directionRaw = direction;
        // Nếu animation null, sẽ lấy animation down khi render
    }

    public void setAnimDown(Animation<TextureRegion> animDown) {
        this.animDown = animDown;
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        switch (direction) {
            case "up": y += speed * delta; break;
            case "down": y -= speed * delta; break;
            case "left": x -= speed * delta; break;
            case "right": x += speed * delta; break;
        }
        hitbox.setPosition(x, y);
        stateTime += delta;
        // Nếu ra khỏi map thì biến mất
        if (x < MIN_X || x > MAX_X || y < MIN_Y || y > MAX_Y) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        Animation<TextureRegion> animToDraw = animation;
        if (animToDraw == null && animDown != null) {
            animToDraw = animDown;
        }
        if (animToDraw != null) {
            TextureRegion frame = animToDraw.getKeyFrame(stateTime, false);

            float regionWidth = frame.getRegionWidth();
            float regionHeight = frame.getRegionHeight();

            float width, height, scale;
            float targetBase = 0.75f; // Tăng kích thước mong muốn lên (ví dụ 0.75f)

            if (directionRaw.equals("left") || directionRaw.equals("right")) {
                // Scale theo chiều cao
                scale = targetBase / regionHeight;
            } else {
                // Scale theo chiều ngang
                scale = targetBase / regionWidth;
            }
            width = regionWidth * scale;
            height = regionHeight * scale;

            float drawX = x - width / 2f;
            float drawY = y - height / 2f;
            float originX = width / 2f;
            float originY = height / 2f;
            float rotation = 0f;

            batch.draw(frame, drawX, drawY, originX, originY, width, height, 1f, 1f, rotation);
        }
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

    public void setInactive() {
        this.active = false;
    }
} 
