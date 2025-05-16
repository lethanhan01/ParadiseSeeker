// ✅ Player.java
package com.paradise_seeker.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.skill.*;

public class Player extends Character {
    public PlayerSkill playerSkill1;
    public PlayerSkill playerSkill2;
    public Weapon weapon;

    private Animation<TextureRegion> runUp, runDown, runLeft, runRight;
    private Animation<TextureRegion> attackUp, attackDown, attackLeft, attackRight;
    private TextureRegion currentFrame;
    private float stateTime = 0f;
    private String direction = "down";
    private boolean isMoving = false;
    private boolean isAttacking = false;

    private boolean isDashing = false;
    private float dashCooldown = 1.0f;
    private float dashTimer = 0f;
    private float dashDistance = 50f;
    private boolean menuOpen = false;
    private boolean isPaused = false;

    public Player(Rectangle bounds) {
        super(bounds, 100, 50, 10, 5f);
        loadAnimations();
        this.playerSkill1 = new PlayerSkill();
        this.playerSkill2 = new PlayerSkill();
    }

    private void loadAnimations() {
        runDown = loadAnimation("images/Entity/characters/player/char_run_down_anim_strip_6.png");
        runUp = loadAnimation("images/Entity/characters/player/char_run_up_anim_strip_6.png");
        runLeft = loadAnimation("images/Entity/characters/player/char_run_left_anim_strip_6.png");
        runRight = loadAnimation("images/Entity/characters/player/char_run_right_anim_strip_6.png");

        attackDown = loadAnimation("images/Entity/characters/player/char_attack_down_anim_strip_6.png");
        attackUp = loadAnimation("images/Entity/characters/player/char_attack_up_anim_strip_6.png");
        attackLeft = loadAnimation("images/Entity/characters/player/char_attack_left_anim_strip_6.png");
        attackRight = loadAnimation("images/Entity/characters/player/char_attack_right_anim_strip_6.png");

        currentFrame = runDown.getKeyFrame(0);
    }

    private Animation<TextureRegion> loadAnimation(String filePath) {
        Texture sheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 6, sheet.getHeight());
        return new Animation<>(0.1f, tmp[0]);
    }

    public void regenMana(float deltaTime) {
        if (mp < 100) {
            mp += 5 * deltaTime;
        }
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        regenMana(deltaTime);
        dashTimer -= deltaTime;
        if (isMoving || isAttacking) {
            stateTime += deltaTime;
        } else {
            stateTime = 0;
        }

        if (isAttacking) {
            Animation<TextureRegion> currentAttack = getAttackAnimationByDirection();
            if (currentAttack.isAnimationFinished(stateTime)) {
                isAttacking = false;
                stateTime = 0;
            }
        }
    }

    private void handleInput(float deltaTime) {
        if (isPaused || isAttacking) return;

        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        isMoving = len > 0;

        if (isMoving) {
            bounds.x += (dx / len) * speed * deltaTime;
            bounds.y += (dy / len) * speed * deltaTime;
            if (Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 0 ? "right" : "left";
            } else {
                direction = dy > 0 ? "up" : "down";
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashTimer <= 0) {
            if (isMoving) {
                bounds.x += (dx / len) * dashDistance;
                bounds.y += (dy / len) * dashDistance;
                dashTimer = dashCooldown;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isAttacking = true;
            stateTime = 0;
        }
    }

    private Animation<TextureRegion> getAttackAnimationByDirection() {
        switch (direction) {
            case "up": return attackUp;
            case "down": return attackDown;
            case "left": return attackLeft;
            case "right": return attackRight;
        }
        return attackDown;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isAttacking) {
            currentFrame = getAttackAnimationByDirection().getKeyFrame(stateTime, false);
            float scaledWidth = bounds.width * 2f;
            float scaledHeight = bounds.height * 2f;
            float drawX = bounds.x - (scaledWidth - bounds.width) / 2f;
            float drawY = bounds.y - (scaledHeight - bounds.height) / 2f;
            batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
        } else {
            switch (direction) {
                case "up": currentFrame = runUp.getKeyFrame(stateTime, true); break;
                case "down": currentFrame = runDown.getKeyFrame(stateTime, true); break;
                case "left": currentFrame = runLeft.getKeyFrame(stateTime, true); break;
                case "right": currentFrame = runRight.getKeyFrame(stateTime, true); break;
            }
            batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    @Override public void move() {}
    @Override public void onDeath() { System.out.println("Player đã chết!"); }
    @Override public void onCollision(Collidable other) {}

    public void castSkill1(int x, int y) {
        if (mp >= playerSkill1.getManaCost()) {
            mp -= playerSkill1.getManaCost();
            playerSkill1.castSkill(atk, x, y);
        }
    }

    public void castSkill2(Character target) {
        if (mp >= playerSkill2.getManaCost()) {
            mp -= playerSkill2.getManaCost();
            playerSkill2.castSkill(atk, target);
        }
    }

    public boolean isPaused() { return isPaused; }
    public boolean isMenuOpen() { return menuOpen; }
}
