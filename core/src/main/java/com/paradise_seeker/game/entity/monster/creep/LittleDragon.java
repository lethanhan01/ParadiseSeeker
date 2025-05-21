package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.paradise_seeker.game.entity.Monster;

public class LittleDragon extends Monster {

    public LittleDragon(Rectangle bounds) {
        super(
            bounds,
            new Texture(Gdx.files.internal("images/Entity/characters/monsters/creep/Little_Dragon/little_dragon.png")),
            bounds.width * 3,
            bounds.height * 3
        );
        this.hp = 40;
        this.atk = 6;
        this.speed = 3.2f;
    }
}
