package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Monster implements Renderable, Collidable {
    public Rectangle bounds;
    public int hp;
    public float speed;
    public int cleaveDamage;

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
    protected float cleaveRange; // ❗Không gán = 4f

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
    public float OFFSET;

    public Monster(float x, float y, int hp, float speed, int cleaveDamage, float offset) {
        this.hp = hp;
        this.speed = speed;
        this.cleaveDamage = cleaveDamage;
        this.OFFSET = offset;
        this.currentFrame = null;
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isDead() {
        return isDead;
    }

    public void update(float deltaTime) {
        if (player == null || player.isDead) return;

        if (isDead) {
            stateTime += deltaTime;
            return;
        }

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
                if (!isCleaving) {
                    approachPlayer(deltaTime);
                }
                if (isPlayerInCleaveRange() && cleaveTimer <= 0f) attackPlayer();
                return;
            }
        }

        returnToSpawn(deltaTime);
    }

    private boolean isPlayerInCleaveRange() {
        float bossCenterX = bounds.x + bounds.width / 2;
        float bossCenterY = bounds.y + bounds.height / 2;
        float playerCenterX = player.bounds.x + player.bounds.width / 2;
        float playerCenterY = player.bounds.y + player.bounds.height / 2;

        float dx = bossCenterX - playerCenterX;
        float dy = bossCenterY - playerCenterY;

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= cleaveRange;
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
                if (!isCleaving) {
                    stateTime = 0f;
                }
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
        float drawY = bounds.y + OFFSET  - (spriteHeight - bounds.height) / 2f;
        batch.draw(currentFrame, drawX, drawY, spriteWidth, spriteHeight);
    }

    public void onDeath() {
        isDead = true;
        stateTime = 0f;
        deathPlayed = true;
    }

    @Override
    public void onCollision(Player player) {
        // Đẩy player ra xa khỏi monster
        float dx = player.bounds.x + player.bounds.width / 2 - (bounds.x + bounds.width / 2);
        float dy = player.bounds.y + player.bounds.height / 2 - (bounds.y + bounds.height / 2);
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist == 0) return;

        float pushAmount = 1f;
        player.bounds.x += (dx / dist) * pushAmount;
        player.bounds.y += (dy / dist) * pushAmount;

        // Cũng nên xử lý hiệu ứng bị hit
        if (player.isShielding) {
            player.isShieldedHit = true;
        } else {
            player.isHit = true;
            player.stateTime = 0;
        }
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

    protected abstract void loadAnimations();
}
