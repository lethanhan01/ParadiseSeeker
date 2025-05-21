package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.paradise_seeker.game.entity.Monster;

public class DemonSoldier extends Monster {

    public DemonSoldier(Rectangle bounds) {
        super(
            bounds,
            new Texture(Gdx.files.internal("images/Entity/characters/monsters/creep/Demon_Soldier/demon_soldier.png")),
            bounds.width * 3,
            bounds.height * 3
        );
        this.hp = 50;
        this.atk = 8;
        this.speed = 3f;
    }
}
