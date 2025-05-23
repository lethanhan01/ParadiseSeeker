package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class EvilPlant extends Monster {
    public EvilPlant(float x, float y) {
    	super(x, y, 50, 1.2f, 12, 0.5f); // hp, speed, cleavedamage, offset
    	this.bounds = new Rectangle(x, y, 2f, 2f);
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 4f;
        this.spriteHeight = 4f;
        loadAnimations();
        this.currentFrame = idleLeft.getKeyFrame(0f);
        this.cleaveRange = 2.5f; // Nhỏ hơn Boss

    }

    @Override
    protected void loadAnimations() {
        walkLeft = loadAnimation("images/Entity/characters/monsters/creep/map2/evil_plant/idle/", "idle", 8, ".png", 1);
        walkRight = walkLeft; // Không có ảnh walk riêng nên dùng idle

        idleLeft = walkLeft;
        idleRight = walkRight;

        cleaveRight = loadAnimation("images/Entity/characters/monsters/creep/map2/evil_plant/atkleft/", "attack_left", 8, ".png", 1);
        cleaveLeft = loadAnimation("images/Entity/characters/monsters/creep/map2/evil_plant/atkright/", "attack_right", 8, ".png", 1);

        takeHitLeft = loadAnimation("images/Entity/characters/monsters/creep/map2/evil_plant/hit/", "hit", 3, ".png", 1);
        takeHitRight = takeHitLeft;

        deathLeft = idleLeft;
        deathRight = idleRight;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
        super.render(batch);
    }

    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIndex) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = String.format("%s%s%02d%s", folder, prefix, i + startIndex, suffix);
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }
}
