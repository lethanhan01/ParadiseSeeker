package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.Monster;

public class GhostStatic extends Monster {

    public GhostStatic(float x, float y) {
        super(x, y, 28, 1.3f, 6, 0f); // HP, speed, cleaveDamage, offset
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 1.2f;
        this.spriteHeight = 1.6f;
        updateBounds();

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 1.2f;
        updateBounds();
    }

    @Override
    protected float getScaleMultiplier() {
        return 2.0f;
    }

    @Override
    protected void loadAnimations() {
        // Dùng chung 1 animation cho tất cả trạng thái
        Animation<TextureRegion> ghostAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/ghost_static/Dark VFX 2 (48x64)", 16);

        walkRight = ghostAnim;
        walkLeft  = ghostAnim;
        idleRight = ghostAnim;
        idleLeft  = ghostAnim;
        cleaveRight = ghostAnim;
        cleaveLeft  = ghostAnim;
        deathRight = ghostAnim;
        deathLeft  = ghostAnim;
        takeHitRight = ghostAnim;
        takeHitLeft  = ghostAnim;
    }

    // Load animation: basePath + số thứ tự từ 1 đến frameCount + ".png"
    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = basePath + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i - 1] = new TextureRegion(texture);
        }
        return new Animation<>(0.14f, frames);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
        super.render(batch);
    }
}
