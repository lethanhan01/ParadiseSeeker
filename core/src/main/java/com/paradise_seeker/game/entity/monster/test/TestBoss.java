package com.paradise_seeker.game.entity.monster.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class TestBoss extends Monster {
    private int mp;

    public TestBoss(float x, float y) {
        super(new Rectangle(x, y, 2f, 1f), // bounds va chạm nhỏ
              new Texture(Gdx.files.internal("images/Entity/characters/monsters/test/testboss.png")),
              9f, 9f); // sprite hiển thị to
        this.hp = 150;
        this.atk = 15;
        this.speed = 1.5f;
    }

    public int getMp() {
        return mp;
    }

    public void useMp(int cost) {
        mp = Math.max(0, mp - cost);
    }

    public void regenMp(float deltaTime) {
        mp = Math.min(100, mp + (int)(10 * deltaTime));
    }
}
