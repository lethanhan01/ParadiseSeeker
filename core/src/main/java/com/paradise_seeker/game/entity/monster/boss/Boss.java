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

public abstract class Boss implements Renderable, Collidable {
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
    public float cleaveRange = 4f;

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
    public float OFFSET = 2f;

    public Boss(float x, float y, int hp, float speed, int cleaveDamage) {
        this.bounds = new Rectangle(x, y + OFFSET, 3f, 5f);
        this.spawnX = x;
        this.spawnY = y + OFFSET;
        this.spriteWidth = 10f;
        this.spriteHeight = 10f;
        this.hp = hp;
        this.speed = speed;
        this.cleaveDamage = cleaveDamage;
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

    public void onCollision(Player player) {
//        if (!isDead && !player.isDead && !isAggressive) {
//            isAggressive = true;
//            aggroTimer = AGGRO_DURATION;
//        }
//        player.pushBackFrom(bounds);
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

    protected Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount) {
        return loadAnimation(folder, prefix, frameCount, ".png");
    }

    protected Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + 1) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    protected Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIndex) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + startIndex) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }
}
