package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class Boss1 extends Monster {
    public Boss1(float x, float y) {
        super(x, y, 100, 1.5f, 20,3f); // HP, speed, cleaveDamage
        this.bounds = new Rectangle(x, y, 3f, 5f);
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 10f;
        this.spriteHeight = 10f;
        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 5f; // Gán riêng tại đây

    }

    @Override
    protected void loadAnimations() {
        walkRight = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/walk/phai/", "demon_walk_", 12, ".png", 1);
        walkLeft  = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/walk/trai/", "demon_walk_", 12, ".png", 1);

        idleRight = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/idle/phai/", "demon_idle_1 (", 6, ").png", 0);
        idleLeft  = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/idle/trai/", "demon_idle_", 6, ".png", 1);

        cleaveRight = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/cleave/phai/", "demon_cleave_", 15, ".png", 1);
        cleaveLeft  = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/cleave/trai/", "demon_cleave_", 15, ".png", 1);

        takeHitRight = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/take_hit/phai/", "demon_take_hit_", 5, ".png", 1);
        takeHitLeft  = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/take_hit/trai/", "demon_take_hit_", 5, ".png", 1);

        deathRight = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/death/phai/", "demon_death_", 22, ".png", 1);
        deathLeft  = loadAnimationNoPadding("images/Entity/characters/monsters/boss/boss_1/death/trai/", "demon_death_", 22, ".png", 1);
    }

    private Animation<TextureRegion> loadAnimationNoPadding(String folder, String prefix, int frameCount, String suffix, int startIndex) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + startIndex) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }
}
