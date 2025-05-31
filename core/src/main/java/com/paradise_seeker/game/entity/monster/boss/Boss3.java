package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.Monster;

public class Boss3 extends Monster {
    public Boss3(float x, float y) {
        super(x, y, 120, 1.8f, 28, 4f); // Tùy chỉnh stats phù hợp
        this.spriteWidth = 10f;
        this.spriteHeight = 6f;
        updateBounds();
        this.spawnX = x;
        this.spawnY = y;
        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 5f;
        updateBounds();
    }

    @Override
    protected void loadAnimations() {
        // WALK
        walkRight = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/walk/phai/", "walk", 8, ".png", 0);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/walk/trai/", "walk", 8, ".png", 0);

        // IDLE
        idleRight = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/idle/phai/", "idle", 8, ".png", 0);
        idleLeft  = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/idle/trai/", "idle", 8, ".png", 0);

        // CLEAVE (có 2 đòn, mỗi đòn 8 frame)
        cleaveRight = loadComboAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/cleave/phai/", "atk", 2, 8, ".png");
        cleaveLeft  = loadComboAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/cleave/trai/", "atk", 2, 8, ".png");

        // TAKE HIT
        takeHitRight = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/takehit/phai/", "takehit", 3, ".png", 0);
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/takehit/trai/", "takehit", 3, ".png", 0);

        // DEATH
        deathRight = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/death/phai/", "death", 7, ".png", 0);
        deathLeft  = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/death/trai/", "death", 7, ".png", 0);
    }

    // Load animation cơ bản (frame đặt tên liên tục: walk0.png, walk1.png, ...)
    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIdx) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + startIdx) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    // Load animation cleave có nhiều đòn: .../atk0/atk0.png, .../atk1/atk0.png, ...
    private Animation<TextureRegion> loadComboAnimation(String folder, String comboPrefix, int comboCount, int framesPerCombo, String suffix) {
        TextureRegion[] frames = new TextureRegion[comboCount * framesPerCombo];
        int idx = 0;
        for (int combo = 0; combo < comboCount; combo++) {
            String subFolder = folder + comboPrefix + combo + "/";
            for (int frame = 0; frame < framesPerCombo; frame++) {
                String filename = subFolder + comboPrefix + frame + suffix;
                Texture texture = new Texture(Gdx.files.internal(filename));
                frames[idx++] = new TextureRegion(texture);
            }
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    protected float getScaleMultiplier() {
        return 10f;
    }
}
