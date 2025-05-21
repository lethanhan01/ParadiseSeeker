package com.paradise_seeker.game.entity.monster.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class TestElite extends Monster {
    public TestElite(float x, float y) {
        super(new Rectangle(x, y, 3f, 3f),
              new Texture(Gdx.files.internal("images/Entity/characters/monsters/test/testelite.png")));
        this.hp = 60;
        this.atk = 8;
        this.speed = 2f;
    }
}
