package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.skill.Skill;

public abstract class Character {
    public Rectangle bounds;
    public int hp;
    public int mp;
    public int atk;
    public float speed;
    public Skill skill;

    public Character(Rectangle bounds, int hp, int mp, int atk, float speed) {
        this.bounds = bounds;
        this.hp = hp;
        this.mp = mp;
        this.atk = atk;
        this.speed = speed;
    }

    

    // Nhận sát thương
    public void receiveDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
        if (hp == 0) onDeath();
    }

    public abstract void onDeath();
    public abstract void move();
    // Kiểm tra còn sống
    public boolean isAlive() {
        return hp > 0;
    }
    ///////////////////////////////////// BỔ SUNG SAU ////////////////////////////////////
  
    public abstract void render(SpriteBatch batch);

    // Collidable
    public Rectangle getBounds() {
        return bounds;
    }

    public void onCollision(Collidable other) {
        // xử lý va chạm mặc định (vd: đạn bắn trúng)
    }
    
}
