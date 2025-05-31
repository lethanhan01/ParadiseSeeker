package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.Monster;

public class SkeletonEnemy extends Monster {
    public SkeletonEnemy(float x, float y) {
        super(x, y, 35, 2.2f, 8, 0f); // HP, speed, cleaveDamage tuỳ chỉnh
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 1.4f;
        this.spriteHeight = 1.4f;
        updateBounds();
        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 1.6f;
        updateBounds();
    }

    @Override
    protected float getScaleMultiplier() {
        return 2.1f;
    }

    @Override
    protected void loadAnimations() {
        // WALK (chạy): skel_enemy_run1.png ... skel_enemy_run12.png
        walkRight = loadRunAnimation();
        walkLeft  = walkRight;

        // IDLE: skel_enemy1.png ... skel_enemy4.png hoặc skel_enemy_1.png ... skel_enemy_4.png
        idleRight = loadIdleAnimation();
        idleLeft  = idleRight;

        // CLEAVE (tấn công): không có file, dùng idle hoặc run lại
        cleaveRight = walkRight;
        cleaveLeft  = walkLeft;

        // TAKE HIT: skel_enemy_hit1.png ... skel_enemy_hit3.png
        takeHitRight = loadHitAnimation();
        takeHitLeft  = takeHitRight;

        // DEATH: skel_enemy_death1.png ... skel_enemy_death13.png
        deathRight = loadDeathAnimation();
        deathLeft  = deathRight;
    }

    // WALK (RUN) - 12 frames: skel_enemy_run1.png ... skel_enemy_run12.png
    private Animation<TextureRegion> loadRunAnimation() {
        int frameCount = 12;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy_run" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i-1] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    // IDLE: skel_enemy1.png ... skel_enemy4.png (hoặc skel_enemy_1.png ... skel_enemy_4.png)
    private Animation<TextureRegion> loadIdleAnimation() {
        int frameCount = 4;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename1 = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy" + i + ".png";
            String filename2 = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy_" + i + ".png";
            Texture texture = null;
            if (Gdx.files.internal(filename1).exists()) {
                texture = new Texture(Gdx.files.internal(filename1));
            } else if (Gdx.files.internal(filename2).exists()) {
                texture = new Texture(Gdx.files.internal(filename2));
            }
            if (texture != null) {
                frames[i-1] = new TextureRegion(texture);
            } else {
                // Nếu không có ảnh, lặp lại frame đầu
                frames[i-1] = frames[0];
            }
        }
        return new Animation<>(0.14f, frames);
    }

    // HIT: skel_enemy_hit1.png ... skel_enemy_hit3.png
    private Animation<TextureRegion> loadHitAnimation() {
        int frameCount = 3;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy_hit" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i-1] = new TextureRegion(texture);
        }
        return new Animation<>(0.14f, frames);
    }

    // DEATH: skel_enemy_death1.png ... skel_enemy_death13.png
    private Animation<TextureRegion> loadDeathAnimation() {
        int frameCount = 13;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy_death" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i-1] = new TextureRegion(texture);
        }
        return new Animation<>(0.11f, frames);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
        super.render(batch);
    }
}
