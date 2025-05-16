package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.paradise_seeker.game.entity.skill.PlayerSkill1;

public class Player extends Character {
    private Texture texture;
    public PlayerSkill1 playerSkill1;
    public Weapon weapon;

    public Player(Rectangle bounds, int hp, int mp, int atk, float speed) {
        this(bounds, hp, mp, atk, speed, new Texture("player.png"));
    }

    public Player(Rectangle bounds, int hp, int mp, int atk, float speed, Texture texture) {
        super(bounds, hp, mp, atk, speed);
        this.texture = texture;
        this.playerSkill1 = new PlayerSkill1(20, 1000); // 20 mana, 1s cooldown
    }

    public Player(Rectangle bounds) {
        this(bounds, 100, 50, 10, 5f, new Texture("player.png"));
    }

    public void pickWeapon(Weapon w) {
        weapon = w;
        atk += (weapon != null ? weapon.getAttackBonus() : 0);
        speed += (weapon != null ? weapon.getSpeedBonus() : 0);
    }

    public void attack(Character target) {
        if (target.isAlive()) {
            target.receiveDamage(atk);
        }
    }

    public void castSkill(int mouseX, int mouseY) {
        if (mp >= playerSkill1.getManaCost()) {
            float centerX = bounds.x + bounds.width / 2f;
            float centerY = bounds.y + bounds.height / 2f;

            float dx = mouseX - centerX;
            float dy = mouseY - centerY;
            float length = (float) Math.sqrt(dx * dx + dy * dy);
            float dirX = dx / length;
            float dirY = dy / length;

            playerSkill1.castSkill(atk, centerX, centerY, dirX, dirY);
            mp -= playerSkill1.getManaCost();
        } else {
            System.out.println("Not enough mana");
        }
    }

    @Override
    public void move() {
        int dx = 0;
        int dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            dx /= length;
            dy /= length;
            bounds.x += dx * speed;
            bounds.y += dy * speed;
        }
    }

    public void update(float deltaTime) {
        move();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mouse);
            castSkill((int) mouse.x, (int) mouse.y);
        }

        playerSkill1.update(deltaTime);
    }

    @Override
    public void onDeath() {
        System.out.println("Player died");
    }

    @Override
    public void onCollision(Collidable other) {
        // va chạm với item, enemy, ...
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        playerSkill1.render(batch);
    }
}
