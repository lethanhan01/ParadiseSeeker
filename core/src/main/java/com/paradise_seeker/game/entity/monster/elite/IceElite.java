package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class IceElite extends Monster {

    public IceElite(float x, float y) {
        super(x, y, 90, 1.5f, 18, 0f); // HP, speed, cleaveDamage, offset
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 3.0f;
        this.spriteHeight = 3.0f;
        updateBounds(); // Đồng bộ lại bounds

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 2.8f;
        updateBounds();

    }
    @Override
    protected float getScaleMultiplier() {
        return 5f;
    }

    @Override
    protected void loadAnimations() {
        // Cleave (attack) - 14 frames, bắt đầu từ 1
        cleaveRight = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/cleave/phai/1_atk_", 14);
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/cleave/trai/1_atk_", 14);

        // Death - 16 frames, bắt đầu từ 1
        deathRight = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/death/phai/death_", 16);
        deathLeft  = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/death/trai/death_", 16);

        // Idle - 6 frames, bắt đầu từ 1
        idleRight = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/idle/phai/idle_", 6);
        idleLeft  = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/idle/trai/idle_", 6);

        // Take hit - 7 frames, bắt đầu từ 1
        takeHitRight = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/takehit/phai/take_hit_", 7);
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/takehit/trai/take_hit_", 7);

        // Walk - 10 frames, bắt đầu từ 1
        walkRight = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/walk/phai/walk_", 10);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/walk/trai/walk_", 10);
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
