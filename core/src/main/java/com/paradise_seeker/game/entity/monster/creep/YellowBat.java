package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class YellowBat extends Monster {
    public YellowBat(float x, float y) {
        super(x, y, 45, 2.8f, 10,0f);
        this.bounds = new Rectangle(x, y, 2f, 2f);
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 2f;
        this.spriteHeight = 2f;
        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 2f; // Nhỏ hơn Boss

    }

    @Override
    protected void loadAnimations() {
        walkRight = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/fly/", "fly", 7, ".png", 1);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/fly/", "fly", 7, ".png", 1);

        idleRight = walkRight;
        idleLeft  = walkLeft;

        cleaveRight = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/atk/", "attack", 10, ".png", 1);
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/atk/", "attack", 10, ".png", 1);

        takeHitRight = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/hit/", "hit", 3, ".png", 1);
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/hit/", "hit", 3, ".png", 1);

        deathRight = idleRight;
        deathLeft  = idleLeft;
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
