package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Monster;

public class DemonKing extends Monster {
    public DemonKing(float x, float y) {
        super(new Rectangle(x, y, 2f, 1f),
              new Texture(Gdx.files.internal("images/Entity/characters/monsters/elite/Demon_King/Demon_King.png")),
              6f, 6f);
        this.hp = 70;
        this.atk = 10;
        this.speed = 2.3f;
    }
}
