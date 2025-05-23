package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.Renderable;

public class TitanKing implements Renderable, Collidable {
    public Rectangle bounds;
    public int hp = 100;
    public float speed = 1.5f;

    public Player player;

    public boolean isAggressive = false;
    public float aggroTimer = 0f;
    public final float AGGRO_DURATION = 5f;
    public boolean isDead = false;

    public float spriteWidth;
    public float spriteHeight;
    public float spawnX;
    public float spawnY;

    public boolean pendingCleaveHit = false;
    public boolean cleaveDamageDealt = false;
    public Animation<TextureRegion> cleaveLeft, cleaveRight;
    public boolean isCleaving = false;
    public float cleaveDuration = 1.2f;
    public float cleaveTimer = 0f;
    public int cleaveDamage = 20;
    public float cleaveRange = 5f;

    public Animation<TextureRegion> deathLeft, deathRight;
    public boolean deathPlayed = false;
    public Animation<TextureRegion> idleLeft, idleRight;
    public Animation<TextureRegion> walkLeft, walkRight;
    public Animation<TextureRegion> takeHitLeft, takeHitRight;
    public TextureRegion currentFrame;
    public boolean facingRight = true;
    public boolean isTakingHit = false;
    public float takeHitDuration = 0.5f;
    public float takeHitTimer = 0f;
    public float stateTime = 0f;
    public Vector2 lastPosition = new Vector2();
    public boolean isMoving = false;

    public TitanKing(float x, float y) {
        this.bounds = new Rectangle(x, y, 2f, 2f);
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 10f;
        this.spriteHeight = 10f;
        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isDead() {
        return isDead;
    }

    public void update(float deltaTime) {
        if (isDead || player == null || player.isDead) return;

        isMoving = lastPosition.dst(bounds.x, bounds.y) > 0.0001f;
        lastPosition.set(bounds.x, bounds.y);

        stateTime += deltaTime;
        if (isTakingHit) {
            takeHitTimer -= deltaTime;
            if (takeHitTimer <= 0f) isTakingHit = false;
        }

        if (isCleaving) {
            if (pendingCleaveHit && !cleaveDamageDealt && stateTime >= cleaveDuration * 0.8f) {
                if (player != null && !player.isDead) {
                    float dx = player.bounds.x + player.bounds.width / 2 - (bounds.x + bounds.width / 2);
                    float dy = player.bounds.y + player.bounds.height / 2 - (bounds.y + bounds.height / 2);
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    if (dist <= cleaveRange) player.takeDamage(cleaveDamage);
                }
                cleaveDamageDealt = true;
            }
            cleaveTimer -= deltaTime;
            if (cleaveTimer <= 0f) {
                isCleaving = false;
                pendingCleaveHit = false;
                cleaveDamageDealt = false;
            }
        }

        if (isAggressive) {
            aggroTimer -= deltaTime;
            if (aggroTimer <= 0f && !isNearPlayer()) isAggressive = false;
            else {
                approachPlayer(deltaTime);
                if (isNearPlayer() && cleaveTimer <= 0f) attackPlayer();
                return;
            }
        }

        returnToSpawn(deltaTime);
    }

    public void attackPlayer() {
        useCleaveSkill();
    }

    public void takeDamage(int damage) {
        if (damage > 0) {
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
            hp = Math.max(0, hp - damage);
            if (hp == 0 && !isDead) onDeath();
            else {
                isTakingHit = true;
                takeHitTimer = takeHitDuration;
                stateTime = 0f;
            }
        }
    }

    public void useCleaveSkill() {
        if (!isCleaving && !isDead) {
            isCleaving = true;
            cleaveTimer = cleaveDuration;
            stateTime = 0f;
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
            pendingCleaveHit = true;
            cleaveDamageDealt = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (isDead) currentFrame = (facingRight ? deathRight : deathLeft).getKeyFrame(stateTime, false);
        else if (isCleaving) currentFrame = (facingRight ? cleaveRight : cleaveLeft).getKeyFrame(stateTime, false);
        else if (isTakingHit) currentFrame = (facingRight ? takeHitRight : takeHitLeft).getKeyFrame(stateTime, false);
        else if (!isMoving) currentFrame = (facingRight ? idleRight : idleLeft).getKeyFrame(stateTime, true);
        else currentFrame = (facingRight ? walkRight : walkLeft).getKeyFrame(stateTime, true);

        if (player != null) facingRight = player.bounds.x > bounds.x;

        float drawX = bounds.x - (spriteWidth - bounds.width) / 2f;
        float drawY = bounds.y - (spriteHeight - bounds.height) / 2f;
        batch.draw(currentFrame, drawX, drawY, spriteWidth, spriteHeight);
    }

    public void onDeath() {
        isDead = true;
        stateTime = 0f;
        deathPlayed = true;
    }

    public void onCollision(Player player) {
        if (!isDead && !player.isDead && !isAggressive) {
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
        }
        player.pushBackFrom(bounds);
    }

    private boolean isNearPlayer() {
        float thisCenterX = bounds.x + bounds.width / 2;
        float thisCenterY = bounds.y + bounds.height / 2;
        float playerCenterX = player.bounds.x + player.bounds.width / 2;
        float playerCenterY = player.bounds.y + player.bounds.height / 2;
        float dx = thisCenterX - playerCenterX;
        float dy = thisCenterY - playerCenterY;
        return Math.sqrt(dx * dx + dy * dy) < Math.max(bounds.width, bounds.height);
    }

    private void approachPlayer(float deltaTime) {
        float dx = player.bounds.x - bounds.x;
        float dy = player.bounds.y - bounds.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance > 0.1f) {
            float moveX = (dx / distance) * speed * deltaTime;
            float moveY = (dy / distance) * speed * deltaTime;
            bounds.x += moveX;
            bounds.y += moveY;
        }
    }

    private void returnToSpawn(float deltaTime) {
        float dx = spawnX - bounds.x;
        float dy = spawnY - bounds.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance > 0.1f) {
            float moveX = (dx / distance) * speed * deltaTime;
            float moveY = (dy / distance) * speed * deltaTime;
            bounds.x += moveX;
            bounds.y += moveY;
        }
    }

    private void loadAnimations() {
        walkRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/walk/phai/", "demon_walk_", 12);
        walkLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/walk/trai/", "demon_walk_", 12);
        idleRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/idle/phai/", "demon_idle_1 (", 6, ").png", 0);
        idleLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/idle/trai/", "demon_idle_", 6, ".png");
        cleaveRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/cleave/phai/", "demon_cleave_", 15, ".png");
        cleaveLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/cleave/trai/", "demon_cleave_", 15, ".png");
        takeHitRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/take_hit/phai/", "demon_take_hit_", 5);
        takeHitLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/take_hit/trai/", "demon_take_hit_", 5);
        deathRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/death/phai/", "demon_death_", 22, ".png");
        deathLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/death/trai/", "demon_death_", 22, ".png");
    }

    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount) {
        return loadAnimation(folder, prefix, frameCount, ".png");
    }

    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + 1) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIndex) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + startIndex) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }
}
