package com.paradise_seeker.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Monster extends Character implements Renderable, Collidable {

    private Texture texture;
    public Player player;

    private float attackCooldown = 1f;
    private float attackTimer = 0f;
    private boolean isDead = false;

    private boolean isAggressive = false;
    private float aggroTimer = 0f;
    private final float AGGRO_DURATION = 5f;
    protected float spriteWidth;
    protected float spriteHeight;

    public Monster(Rectangle bounds, Texture texture, float spriteWidth, float spriteHeight) {
        super(bounds, 50, 20, 8, 3f); // hp, atk, speed, mana...
        this.texture = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }


    public void update(float deltaTime) {
        if (isDead || player == null || player.isDead) return;

        attackTimer -= deltaTime;

        if (isAggressive) {
            aggroTimer -= deltaTime;

            // Vẫn tiếp tục đuổi nếu còn thời gian hoặc còn gần người chơi
            if (aggroTimer <= 0f && !isNearPlayer()) {
                isAggressive = false;
                return;
            }

            approachPlayer(deltaTime);

            if (isNearPlayer() && attackTimer <= 0) {
                attackPlayer();
                attackTimer = attackCooldown;
            }
        }

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

            System.out.println("Monster is moving to: (" + bounds.x + ", " + bounds.y + ")");
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

        float effectiveRange = Math.max(bounds.width, bounds.height); // hoặc bạn có thể nhân thêm 0.8f để không nhạy quá
        boolean near = distance < effectiveRange;

        if (near) {
            System.out.println("Monster is near player → ATTACK!");
        }

        return near;
    }


    private void attackPlayer() {
        if (!player.isDead) {
            System.out.println("Monster is ATTACKING!");
            player.takeDamage(atk);

            // Kích hoạt hoặc duy trì trạng thái aggressive
            isAggressive = true;
            aggroTimer = AGGRO_DURATION; // Reset lại thời gian đuổi đánh
        }
    }

    @Override
    public void onCollision(Player player) {
        if (!isDead && !player.isDead && !isAggressive) {
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
        }
        player.pushBackFrom(bounds); // bạn có thể định nghĩa hàm pushBackFrom

        // optional: đẩy lùi player
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
            System.out.println("Monster took damage, started chasing.");
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
