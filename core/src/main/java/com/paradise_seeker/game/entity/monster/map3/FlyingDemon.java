package com.paradise_seeker.game.entity.monster.map3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.paradise_seeker.game.entity.Monster;
import com.paradise_seeker.game.entity.Player;

public class FlyingDemon extends Monster {

    private class Projectile {
    Rectangle bounds;
    TextureRegion texture;
    float speed = 3.5f;
    boolean active = true;
    float velocityX;
    float velocityY;

    Projectile(float x, float y, float targetX, float targetY, boolean facingRight) {
        String path = facingRight ?
            "images/Entity/characters/monsters/elite/map3/flying_demon/right/atk/projectile_right.png" :
            "images/Entity/characters/monsters/elite/map3/flying_demon/left/atk/projectile.png";
        texture = new TextureRegion(new Texture(Gdx.files.internal(path)));
        bounds = new Rectangle(x, y, 0.5f, 0.5f);

        // Tính hướng bay
        float dx = targetX - x;
        float dy = targetY - y;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len != 0) {
            velocityX = (dx / len) * speed;
            velocityY = (dy / len) * speed;
        } else {
            velocityX = speed * (facingRight ? 1 : -1);
            velocityY = 0;
        }
    }

    void update(float deltaTime) {
        if (!active) return;
        bounds.x += velocityX * deltaTime;
        bounds.y += velocityY * deltaTime;
        if (bounds.overlaps(player.bounds)) {
            player.takeDamage(15); // Sát thương đạn
            active = false;
        }
    }

    void render(SpriteBatch batch) {
        if (active) batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}

    private Array<Projectile> projectiles = new Array<>();

    public FlyingDemon(float x, float y) {
        super(x, y, 80, 2.2f, 12, 0f); // HP, speed, cleaveDamage, offset
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 2.5f;
        this.spriteHeight = 2.5f;
        updateBounds(); // Đồng bộ lại bounds

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 2.5f;
        updateBounds();

    }
    @Override
    protected float getScaleMultiplier() {
        return 5f;
    }

    @Override
    protected void loadAnimations() {
        // Fly (walk)
        walkRight = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/fly/flyingdemon_fly", 4);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/fly/flyingdemon_fly", 4);

        // Idle
        idleRight = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/idle/flyingdemon_idle", 4);
        idleLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/idle/flyingdemon_idle", 4);

        // Attack (cleave)
        cleaveRight = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/atk/flyingdemon_atk", 8);
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/atk/flyingdemon_atk", 8);

        // Get hit
        takeHitRight = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/gethit/flyingdemon_gethit", 4);
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/gethit/flyingdemon_gethit", 4);

        // Death
        deathRight = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/death/flyingdemon_death", 7);
        deathLeft  = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/death/flyingdemon_death", 7);
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + i + ".png"; // Index từ 0 -> ĐÚNG với file thực tế
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (Projectile p : projectiles) {
            p.update(deltaTime);
        }

        for (int i = projectiles.size - 1; i >= 0; i--) {
            if (!projectiles.get(i).active) {
                projectiles.removeIndex(i);
            }
        }

        if (isCleaving && cleaveTimer > 0 && !cleaveDamageDealt && stateTime >= cleaveDuration * 0.2f) {
            spawnProjectile();
            cleaveDamageDealt = true;
        }
    }

    private void spawnProjectile() {
        float bulletX = facingRight ? bounds.x + bounds.width : bounds.x - 0.5f;
        float bulletY = bounds.y + bounds.height / 2 - 0.25f;
        float targetX = player.bounds.x + player.bounds.width / 2;
        float targetY = player.bounds.y + player.bounds.height / 2;
        projectiles.add(new Projectile(bulletX, bulletY, targetX, targetY, facingRight));
    }


    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
        super.render(batch);
        for (Projectile p : projectiles) {
            p.render(batch);
        }
    }
}
