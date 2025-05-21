package com.paradise_seeker.game.entity.monster.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class TestCreep extends Monster {
    public TestCreep(float x, float y) {
        super(new Rectangle(x, y, 1f, 1f),
              new Texture(Gdx.files.internal("images/Entity/characters/monsters/test/testcreep.png")));
        this.hp = 20;
        this.atk = 4;
        this.speed = 3f;
    }
}
