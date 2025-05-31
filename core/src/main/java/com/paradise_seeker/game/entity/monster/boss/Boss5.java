package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.Monster;

public class Boss5 extends Monster {
    public Boss5(float x, float y) {
        super(x, y, 160, 1.2f, 35, 5f); // HP, speed, cleaveDamage, cleaveRange tuỳ chỉnh theo game bạn
        this.spriteWidth = 12f;
        this.spriteHeight = 7f;
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
        // WALK (tên file là run0.png, run1.png, ...)
        walkRight = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/walk/phai/", "run", 8, ".png", 0);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/walk/trai/", "run", 8, ".png", 0);

        // IDLE
        idleRight = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/idle/phai/", "idle", 8, ".png", 0);
        idleLeft  = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/idle/trai/", "idle", 8, ".png", 0);

        // CLEAVE: mỗi hướng có 3 đòn (atk1, atk2, atk3)
        cleaveRight = loadBoss5CleaveCombo("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/cleave/phai/");
        cleaveLeft  = loadBoss5CleaveCombo("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/cleave/trai/");

        // TAKEHIT
        takeHitRight = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/takehit/phai/", "takehit", 4, ".png", 0);
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/takehit/trai/", "takehit", 4, ".png", 0);

        // DEATH
        deathRight = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/death/phai/", "death", 6, ".png", 0);
        deathLeft  = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/death/trai/", "death", 6, ".png", 0);
    }

    // Load animation bình thường: run0.png, run1.png...
    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIdx) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + startIdx) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    // Load CLEAVE đặc biệt cho Boss5 (3 combo, tên file khác nhau)
    private Animation<TextureRegion> loadBoss5CleaveCombo(String folder) {
        int comboCount = 3;
        int framesPerCombo = 4;
        TextureRegion[] frames = new TextureRegion[comboCount * framesPerCombo];
        int idx = 0;
        for (int combo = 1; combo <= comboCount; combo++) {
            String subFolder = folder + "atk" + combo + "/";
            for (int frame = 0; frame < framesPerCombo; frame++) {
                String filename;
                // Phải xử lý đúng tên file cho từng combo
                if (combo == 1) {
                    // .../atk1/atk0.png, atk1.png, atk2.png, atk3.png
                    filename = subFolder + "atk" + frame + ".png";
                } else if (combo == 2) {
                    // .../atk2/atk1_0.png ... atk1_3.png
                    filename = subFolder + "atk1_" + frame + ".png";
                } else { // combo == 3
                    // .../atk3/atk2_0.png ... atk2_3.png
                    filename = subFolder + "atk2_" + frame + ".png";
                }
                Texture texture = new Texture(Gdx.files.internal(filename));
                frames[idx++] = new TextureRegion(texture);
            }
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    protected float getScaleMultiplier() {
        return 12f;
    }
}
