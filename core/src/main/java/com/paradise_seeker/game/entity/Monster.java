package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Monster extends Character {
    private Texture texture;

    public Monster(Rectangle bounds, Texture texture) {
        super(bounds, 50, 0, 8, 3f);
        this.texture = texture;
    }

    public Monster(Rectangle bounds) {
        this(bounds, new Texture("monster.png")); // giả sử có sẵn file monster.png
    }

    @Override
    protected void onDeath() {
        System.out.println("Monster chết, rơi loot");
    }

    @Override
    public void castSkill(int atkPower, Character target) {
        // AI tấn công đơn
    }

    @Override
    public void castSkill(int atkPower, int x, int y) {
        // tấn công vùng (nếu có)
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void update(float deltaTime) {
        // AI di chuyển, tấn công, tuần tra
    }
    //hello
}
