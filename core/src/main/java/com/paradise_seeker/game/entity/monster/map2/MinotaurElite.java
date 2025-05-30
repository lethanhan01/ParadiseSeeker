package com.paradise_seeker.game.entity.monster.map2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class MinotaurElite extends Monster {

    public MinotaurElite(float x, float y) {
        super(x, y, 70, 1.6f, 15, 0f); // HP, speed, cleaveDamage, offset
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 2.8f;
        this.spriteHeight = 2.8f;
        updateBounds(); // Đồng bộ lại bounds

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 2.5f;
        updateBounds();

    }
    @Override
    protected float getScaleMultiplier() {
        return 5f;
    }

    @Override
    protected void loadAnimations() {
        // Cleave (attack) - 16 frames, bắt đầu từ 1
        cleaveRight = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/cleave/phai/atk_1_", 16);
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/cleave/trai/atk_1_", 16);

        // Idle - 16 frames, bắt đầu từ 1
        idleRight = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/idle/phai/idle_", 16);
        idleLeft  = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/idle/trai/idle_", 16);

        // Walk - 12 frames, bắt đầu từ 1
        walkRight = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/walk/phai/walk_", 12);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/walk/trai/walk_", 12);

        // Tạm dùng idle cho takeHit và death
        takeHitRight = idleRight;
        takeHitLeft  = idleLeft;
        deathRight = idleRight;
        deathLeft  = idleLeft;
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + (i + 1) + ".png"; // Bắt đầu từ 1
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
        super.render(batch);
    }
}
