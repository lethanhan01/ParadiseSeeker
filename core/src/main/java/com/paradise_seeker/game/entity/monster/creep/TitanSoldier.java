package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.paradise_seeker.game.entity.Monster;

public class TitanSoldier extends Monster {

    public TitanSoldier(Rectangle bounds) {
        super(
            bounds,
            new Texture(Gdx.files.internal("images/Entity/characters/monsters/creep/Titan_Soldier/titan_soldier.png")),
            bounds.width * 3,
            bounds.height * 3
        );
        this.hp = 60;
        this.atk = 10;
        this.speed = 2.5f;
    }
}
