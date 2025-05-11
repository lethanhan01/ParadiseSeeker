package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Character {
    private Texture texture;

    public Player(Rectangle bounds, int maxHp, int maxMp, int atk, float speed, Texture texture) {
        super(bounds, maxHp, maxMp, atk, speed);
        this.texture = texture;
    }

    public Player(Rectangle bounds) {
        this(bounds, 100, 50, 10, 5f, new Texture("player.png")); // ví dụ load từ asset
    }

    @Override
    protected void onDeath() {
        System.out.println("Player chết");
    }

    @Override
    public void castSkill(int atkPower, Character target) {
        // logic đánh đòn
    }

    @Override
    public void castSkill(int atkPower, int x, int y) {
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void update(float deltaTime) {
        // ví dụ hồi mana, xử lý animation, input, v.v.
    }
}
