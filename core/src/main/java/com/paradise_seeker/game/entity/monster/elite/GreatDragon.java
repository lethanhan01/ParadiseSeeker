package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class GreatDragon extends Monster {
    public GreatDragon(float x, float y) {
        super(new Rectangle(x, y, 2f, 1f),
              new Texture(Gdx.files.internal("images/Entity/characters/monsters/elite/Great_Dragon/Great_Dragon.png")),
              6f, 6f);
        this.hp = 80;
        this.atk = 12;
        this.speed = 2.2f;
    }
}
