package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.paradise_seeker.game.entity.Monster;

public class TitanKing extends Monster {
	private Animation<TextureRegion> cleaveLeft, cleaveRight;
	private boolean isCleaving = false;
	private float cleaveDuration = 1.2f; // 15 frame * 0.08s
	private float cleaveTimer = 0f;
	private int cleaveDamage = 20;
	private float cleaveRange = 3f;

	private Animation<TextureRegion> deathLeft, deathRight;
	private boolean deathPlayed = false; // để chỉ phát death 1 lần

	private Animation<TextureRegion> idleLeft, idleRight;

    private Animation<TextureRegion> walkLeft, walkRight;
    private Animation<TextureRegion> takeHitLeft, takeHitRight;
    private TextureRegion currentFrame;
    private boolean facingRight = true;

    private boolean isTakingHit = false;
    private float takeHitDuration = 0.5f;
    private float takeHitTimer = 0f;
    private float stateTime = 0f;
    private Vector2 lastPosition = new Vector2();
    private boolean isMoving = false;

    public TitanKing(float x, float y) {
        super(new Rectangle(x, y, 3f, 2f), null, 10f, 10f);
        this.hp = 80;
        this.atk = 10;
        this.speed = 1.5f;

        walkRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/walk/phai/", "demon_walk_", 12);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/boss/boss_1/walk/trai/", "demon_walk_", 12);

        takeHitRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/take_hit/phai/", "demon_take_hit_", 5);
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/boss/boss_1/take_hit/trai/", "demon_take_hit_", 5);
        idleRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/idle/phai/",
                "demon_idle_1 (", 6, ").png", 0);
        idleLeft  = loadAnimation("images/Entity/characters/monsters/boss/boss_1/idle/trai/", "demon_idle_", 6, ".png");
        deathLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/death/trai/", "demon_death_", 22, ".png");
        deathRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/death/phai/", "demon_death_", 22, ".png");
        cleaveRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/cleave/phai/", "demon_cleave_", 15, ".png");
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/boss/boss_1/cleave/trai/", "demon_cleave_", 15, ".png");

        currentFrame = walkRight.getKeyFrame(0f);
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


    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + 1) + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        isMoving = lastPosition.dst(bounds.x, bounds.y) > 0.01f;
        lastPosition.set(bounds.x, bounds.y);

        stateTime += deltaTime;

        if (isTakingHit) {
            takeHitTimer -= deltaTime;
            if (takeHitTimer <= 0f) {
                isTakingHit = false;
                stateTime = 0f;
            }
        }
     // Đếm ngược thời gian chém
        if (isCleaving) {
            cleaveTimer -= deltaTime;
            if (cleaveTimer <= 0f) {
                isCleaving = false;
                stateTime = 0f;
            }
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDead()) {
            Animation<TextureRegion> deathAnim = facingRight ? deathRight : deathLeft;
            currentFrame = deathAnim.getKeyFrame(stateTime, false);
        } else if (isCleaving) {
            Animation<TextureRegion> cleaveAnim = facingRight ? cleaveRight : cleaveLeft;
            currentFrame = cleaveAnim.getKeyFrame(stateTime, false);
        } else if (isTakingHit) {
            currentFrame = facingRight
                ? takeHitRight.getKeyFrame(stateTime, false)
                : takeHitLeft.getKeyFrame(stateTime, false);
        } else if (!isMoving) {
            currentFrame = facingRight
                ? idleRight.getKeyFrame(stateTime, true)
                : idleLeft.getKeyFrame(stateTime, true);
        } else {
            currentFrame = facingRight
                ? walkRight.getKeyFrame(stateTime, true)
                : walkLeft.getKeyFrame(stateTime, true);
        }

        // Cập nhật hướng nhìn nếu có player
        if (player != null) {
            facingRight = player.bounds.x > bounds.x;
        }

        float drawX = bounds.x - (spriteWidth - bounds.width) / 2f;
        float drawY = bounds.y - (spriteHeight - bounds.height) / 2f;
        batch.draw(currentFrame, drawX, drawY, spriteWidth, spriteHeight);
    }


    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (damage > 0 && !isDead()) {
            isTakingHit = true;
            takeHitTimer = takeHitDuration;
            stateTime = 0f;
        }
    }
    @Override
    public void onDeath() {
        isDead = true;
        stateTime = 0f;
        deathPlayed = true;
    }
    public void useCleaveSkill() {
        if (!isCleaving && !isDead()) {
            isCleaving = true;
            cleaveTimer = cleaveDuration;
            stateTime = 0f;

            // Gây sát thương nếu player trong phạm vi
            if (player != null && !player.isDead) {
                float dx = player.bounds.x + player.bounds.width / 2 - (bounds.x + bounds.width / 2);
                float dy = player.bounds.y + player.bounds.height / 2 - (bounds.y + bounds.height / 2);
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist <= cleaveRange) {
                    player.takeDamage(cleaveDamage);
                }
            }
        }
    }

}
//    public void useCleaveSkill() {