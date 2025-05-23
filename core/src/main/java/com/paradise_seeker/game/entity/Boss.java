package com.paradise_seeker.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Boss extends Character implements Renderable, Collidable {
    protected float spawnX, spawnY;
    protected float spriteWidth, spriteHeight;

    public Player player;
    public boolean isAggressive = false;
    protected float aggroTimer = 0f;
    protected final float AGGRO_DURATION = 5f;
    protected boolean isDead = false;

    // Animation
    protected Animation<TextureRegion> walkLeft, walkRight;
    protected Animation<TextureRegion> idleLeft, idleRight;
    protected Animation<TextureRegion> cleaveLeft, cleaveRight;
    protected Animation<TextureRegion> deathLeft, deathRight;
    protected Animation<TextureRegion> takeHitLeft, takeHitRight;

    protected TextureRegion currentFrame;
    protected boolean facingRight = true;
    protected boolean isMoving = false;
    protected boolean isTakingHit = false;
    protected float takeHitTimer = 0f;
    protected float takeHitDuration = 0.5f;
    protected boolean deathPlayed = false;

    // Cleave skill
    protected boolean isCleaving = false;
    protected boolean pendingCleaveHit = false;
    protected boolean cleaveDamageDealt = false;
    protected float cleaveDuration = 1.2f;
    protected float cleaveTimer = 0f;
    protected float cleaveRange = 3f;
    protected int cleaveDamage = 50;

    protected float stateTime = 0f;
    protected Vector2 lastPosition = new Vector2();

    public Boss(Rectangle bounds, float spriteWidth, float spriteHeight) {
        super(bounds, 100, 20, 20, 1.5f); // hp, mp, atk, speed
        this.spawnX = bounds.x;
        this.spawnY = bounds.y;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.lastPosition.set(bounds.x, bounds.y);
        loadAnimations();
        currentFrame = walkRight.getKeyFrame(0f);
    }

    protected abstract void loadAnimations();

    
    public void update(float deltaTime) {
        if (isDead || player == null || player.isDead) return;

        stateTime += deltaTime;

        isMoving = lastPosition.dst(bounds.x, bounds.y) > 0.0001f;
        lastPosition.set(bounds.x, bounds.y);

        if (isTakingHit) {
            takeHitTimer -= deltaTime;
            if (takeHitTimer <= 0f) isTakingHit = false;
        }

        if (isAggressive) {
            aggroTimer -= deltaTime;
            if (aggroTimer <= 0f && !isNearPlayer()) {
                isAggressive = false;
            } else {
                approachPlayer(deltaTime);
                if (isNearPlayer() && cleaveTimer <= 0f) {
                    attackPlayer();
                    cleaveTimer = cleaveDuration;
                }
                return;
            }
        }

        returnToSpawn(deltaTime);

        if (isCleaving) {
            if (pendingCleaveHit && !cleaveDamageDealt && stateTime >= cleaveDuration * 0.8f) {
                if (player != null && !player.isDead) {
                    float dx = player.bounds.x + player.bounds.width / 2 - (bounds.x + bounds.width / 2);
                    float dy = player.bounds.y + player.bounds.height / 2 - (bounds.y + bounds.height / 2);
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    if (dist <= cleaveRange) {
                        player.takeDamage(cleaveDamage);
                    }
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
    }

    protected void approachPlayer(float deltaTime) {
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

    protected void returnToSpawn(float deltaTime) {
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

    protected boolean isNearPlayer() {
        float thisCenterX = bounds.x + bounds.width / 2;
        float thisCenterY = bounds.y + bounds.height / 2;
        float playerCenterX = player.bounds.x + player.bounds.width / 2;
        float playerCenterY = player.bounds.y + player.bounds.height / 2;
        float dx = thisCenterX - playerCenterX;
        float dy = thisCenterY - playerCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < Math.max(bounds.width, bounds.height);
    }

    public void useCleaveSkill() {
        if (!isCleaving && !isDead()) {
            isCleaving = true;
            cleaveTimer = cleaveDuration;
            stateTime = 0f;
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
            pendingCleaveHit = true;
            cleaveDamageDealt = false;
        }
    }

    
    public void attackPlayer() {
        useCleaveSkill();
    }

    
    public void takeDamage(int damage) {
        if (damage > 0) {
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
        }

        hp = Math.max(0, hp - damage);
        if (hp == 0 && !isDead) onDeath();
        else {
            isTakingHit = true;
            takeHitTimer = takeHitDuration;
            stateTime = 0f;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDead()) {
            currentFrame = (facingRight ? deathRight : deathLeft).getKeyFrame(stateTime, false);
        } else if (isCleaving) {
            currentFrame = (facingRight ? cleaveRight : cleaveLeft).getKeyFrame(stateTime, false);
        } else if (isTakingHit) {
            currentFrame = (facingRight ? takeHitRight : takeHitLeft).getKeyFrame(stateTime, false);
        } else if (!isMoving) {
            currentFrame = (facingRight ? idleRight : idleLeft).getKeyFrame(stateTime, true);
        } else {
            currentFrame = (facingRight ? walkRight : walkLeft).getKeyFrame(stateTime, true);
        }

        if (player != null) facingRight = player.bounds.x > bounds.x;

        float drawX = bounds.x - (spriteWidth - bounds.width) / 2f;
        float drawY = bounds.y - (spriteHeight - bounds.height) / 2f;
        batch.draw(currentFrame, drawX, drawY, spriteWidth, spriteHeight);
    }

    @Override
    public void move() {} // Boss không dùng move() thủ công

    @Override
    public void onDeath() {
        isDead = true;
        stateTime = 0f;
        deathPlayed = true;
    }

    public boolean isDead() {
        return isDead;
    }

    protected Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, float duration) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String path = folder + prefix + (i + 1) + ".png";
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(path)));
        }
        return new Animation<>(duration, frames);
    }

    protected Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount) {
        return loadAnimation(folder, prefix, frameCount, 0.1f);
    }

    protected Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIndex) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String path = folder + prefix + (i + startIndex) + suffix;
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(path)));
        }
        return new Animation<>(0.1f, frames);
    }
    @Override
    public void onCollision(Player player) {
        if (!isDead && !player.isDead && !isAggressive) {
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
        }
        player.pushBackFrom(bounds);
    }

}
