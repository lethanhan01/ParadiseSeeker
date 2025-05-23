package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Monster extends Character implements Renderable, Collidable {
    private float spawnX;
    private float spawnY;

    private Texture texture;
    public Player player;

    private float attackCooldown = 1f;
    private float attackTimer = 0f;
    public boolean isDead = false;

    public boolean isAggressive = false;
    public float aggroTimer = 0f;
    public final float AGGRO_DURATION = 50f;
    public float spriteWidth;
    public float spriteHeight;

    // Lang thang quanh spawn
    private float wanderTimer = 5f;
    private float wanderCooldown = 5f;
    private float wanderTargetX;
    private float wanderTargetY;

    public Monster(Rectangle bounds, Texture texture, float spriteWidth, float spriteHeight) {
        super(bounds, 50, 20, 8, 3f); // hp, atk, speed, mana...
        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spawnX = bounds.x;
        this.spawnY = bounds.y;
    }

    public void update(float deltaTime) {
        if (isDead || player == null || player.isDead) return;

        attackTimer -= deltaTime;

        if (isAggressive) {
            aggroTimer -= deltaTime;

            if (aggroTimer <= 0f && !isNearPlayer()) {
                isAggressive = false;
            } else {
                approachPlayer(deltaTime);

                if (isNearPlayer() && attackTimer <= 0) {
                    attackPlayer();
                    attackTimer = attackCooldown;
                }
                return;
            }
        }

        wander(deltaTime); // Nếu không aggro thì đi lang thang
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

    private void wander(float deltaTime) {
        // Nếu đi quá xa vùng bán kính 5 ô, quay lại
        float maxDistance = 2f * bounds.width;
        float distFromSpawn = Vector2.dst(bounds.x, bounds.y, spawnX, spawnY);

        if (distFromSpawn > maxDistance) {
            returnToSpawn(deltaTime);
            return;
        }

//        wanderTimer -= deltaTime;
//
//        if (wanderTimer <= 0f) {
//            wanderTimer = wanderCooldown;
//
//            float angle = (float)(Math.random() * Math.PI * 2);
//            wanderTargetX = spawnX + (float)Math.cos(angle) * maxDistance;
//            wanderTargetY = spawnY + (float)Math.sin(angle) * maxDistance;
//        }
//
//        float dx = wanderTargetX - bounds.x;
//        float dy = wanderTargetY - bounds.y;
//        float distance = (float)Math.sqrt(dx * dx + dy * dy);
//
//        if (distance > 0.1f) {
//            float moveX = (dx / distance) * speed * deltaTime;
//            float moveY = (dy / distance) * speed * deltaTime;
//            bounds.x += moveX;
//            bounds.y += moveY;
//
//        }
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

    private boolean isNearPlayer() {
        float thisCenterX = bounds.x + bounds.width / 2;
        float thisCenterY = bounds.y + bounds.height / 2;

        float playerCenterX = player.bounds.x + player.bounds.width / 2;
        float playerCenterY = player.bounds.y + player.bounds.height / 2;

        float dx = thisCenterX - playerCenterX;
        float dy = thisCenterY - playerCenterY;

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float effectiveRange = Math.max(bounds.width, bounds.height);

        boolean near = distance < effectiveRange;


        return near;
    }

    public void attackPlayer() {
        if (!player.isDead) {
            player.takeDamage(atk);
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
        }
    }

    @Override
    public void onCollision(Player player) {
        if (!isDead && !player.isDead && !isAggressive) {
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
        }
        player.pushBackFrom(bounds);
    }

    @Override
    public void move() {
        float randomX = (float) (Math.random() * 2 - 1);
        float randomY = (float) (Math.random() * 2 - 1);
        bounds.x += randomX * speed;
        bounds.y += randomY * speed;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isDead) {
            float drawX = bounds.x - (spriteWidth - bounds.width) / 2f;
            float drawY = bounds.y - (spriteHeight - bounds.height) / 2f;
            batch.draw(texture, drawX, drawY, spriteWidth, spriteHeight);
        }
    }

    @Override
    public void onDeath() {
        isDead = true;
        System.out.println("Monster DIED.");
    }

    public void takeDamage(int damage) {
        if (damage > 0) {
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
        }

        hp = Math.max(0, hp - damage);
        if (hp == 0 && !isDead) {
            onDeath();
        }
    }

    public boolean isDead() {
        return isDead;
    }
    
}
