package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class DevilCreep extends Monster {

    public DevilCreep(float x, float y) {
        super(x, y, 50, 1.8f, 12, 0f); // HP, speed, cleaveDamage, offset
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 2.2f;
        this.spriteHeight = 2.2f;
        updateBounds(); // Đồng bộ lại bounds

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 2.5f;
    }

    protected void loadAnimations() {
        // Cleave (attack) - 16 frame (đúng)
        cleaveRight = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_atk_", 16, ".png");
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_atk_", 16, ".png");

        // Death - 13 frame (đúng)
        deathRight = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_death_", 13, ".png");
        deathLeft  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_death_", 13, ".png");

        // Idle - 5 frame (đúng)
        idleRight = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_idle_", 5, ".png");
        idleLeft  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_idle_", 5, ".png");

        // Move - 7 frame (đã sửa)
        walkRight = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_move_", 7, ".png");
        walkLeft  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_move_", 7, ".png");

        // Take Hit - 4 frame (đã sửa)
        takeHitRight = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_takeDamage_", 4, ".png");
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_takeDamage_", 4, ".png");
    }



    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount, String suffix) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + (i + 1) + suffix;
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
