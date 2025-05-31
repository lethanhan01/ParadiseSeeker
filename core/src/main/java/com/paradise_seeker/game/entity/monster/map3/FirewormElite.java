package com.paradise_seeker.game.entity.monster.map3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class FirewormElite extends Monster {

    public FirewormElite(float x, float y) {
        super(x, y, 100, 1.5f, 20, 0f); // HP, speed, cleaveDamage, offset
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 3.0f;
        this.spriteHeight = 3.0f;
        updateBounds(); // Đồng bộ lại bounds

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 3.0f;
        updateBounds();

    }
    @Override
    protected float getScaleMultiplier() {
        return 5f;
    }

    
    @Override
    protected void loadAnimations() {
        // Walk (9 frame, index 0)
        walkRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_walk/right/walk", 9);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_walk/left/walk", 9);

        // Idle (9 frame)
        idleRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_idle/right/idle", 9);
        idleLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_idle/left/idle", 9);

        // Attack (16 frame)
        cleaveRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_atk/right/fireworm", 16);
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_atk/left/fireworm-left/fireworm", 16);

        // Take Hit (3 frame)
        takeHitRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_hit/right/hit", 3);
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_hit/left/hit", 3);

        // Death (8 frame)
        deathRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_death/right/death", 8);
        deathLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_death/left/death", 8);
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + i + ".png"; // Index bắt đầu từ 0 (khớp tên file)
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
