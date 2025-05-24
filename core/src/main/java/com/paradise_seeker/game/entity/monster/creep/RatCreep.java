package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class RatCreep extends Monster {

    public RatCreep(float x, float y) {
        super(x, y, 30, 2f, 8, 0f); // HP, speed, cleaveDamage, offset
        this.bounds = new Rectangle(x, y, 1.5f, 1.5f); // Rat nhỏ hơn CyanBat
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 1.5f;
        this.spriteHeight = 1.5f;
        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 1.5f;
    }

    @Override
    protected void loadAnimations() {
        // Run (walk) animation
        walkRight = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/right/run/rat_run", 6);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/left/run/rat_run", 6);

        // Idle animation
        idleRight = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/right/idle/rat_idle", 6);
        idleLeft  = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/left/idle/rat_idle", 6);

        // Attack (cleave) animation
        cleaveRight = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/right/atk/rat_atk", 6);
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/left/atk/rat_atk", 6);

        // Hurt (take hit) animation - single frame
        takeHitRight = loadSingleFrame("images/Entity/characters/monsters/creep/map3/rat_creep/right/hurt/rat-hurt-outline.png");
        takeHitLeft  = loadSingleFrame("images/Entity/characters/monsters/creep/map3/rat_creep/left/hurt/rat-hurt-outline.png");

        // Death animation
        deathRight = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/right/death/rat_death", 6);
        deathLeft  = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/left/death/rat_death", 6);
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    private Animation<TextureRegion> loadSingleFrame(String filepath) {
        TextureRegion frame = new TextureRegion(new Texture(Gdx.files.internal(filepath)));
        return new Animation<>(0.1f, frame);
    }


    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
        super.render(batch);
    }
}
