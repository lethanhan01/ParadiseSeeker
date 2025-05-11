package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Character {
    protected Rectangle bounds;
    protected int hp, maxHp;
    protected int mp, maxMp;
    protected int atk;
    protected float speed;
    protected Weapon weapon;

    public Character(Rectangle bounds, int maxHp, int maxMp, int atk, float speed) {
        this.bounds = bounds;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMp = maxMp;
        this.mp = maxMp;
        this.atk = atk;
        this.speed = speed;
    }

    // Di chuyển nhân vật
    public void move(float dx, float dy) {
        bounds.x += dx;
        bounds.y += dy;
    }

    // Nhận sát thương
    public void receiveDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
        if (hp == 0) onDeath();
    }

    // Kiểm tra còn sống
    public boolean isAlive() {
        return hp > 0;
    }

    // Trừ mana
    public void minusMana(int cost) {
        mp = Math.max(0, mp - cost);
    }

    // Nhặt vũ khí
    public void pickWeapon(Weapon w) {
        weapon = w;
        updateStats();
    }

    // Cập nhật lại thuộc tính khi thay đổi trạng thái/vũ khí
    public void updateStats() {
         // atk = baseAtk + (weapon!=null ? weapon.getAttackBonus() : 0);
        // speed = baseSpeed + (weapon!=null ? weapon.getSpeedBonus() : 0f);
    }

    // Chiêu tấn công mục tiêu
    public abstract void castSkill(int atkPower, Character target);
    // Chiêu tấn công theo tọa độ
    public abstract void castSkill(int atkPower, int x, int y);

    // Xử lý logic khi chết
    protected abstract void onDeath();

    // Render
    public abstract void render(SpriteBatch batch);

    // Collidable
    public Rectangle getBounds() {
        return bounds;
    }

    public void onCollision(Collidable other) {
        // xử lý va chạm mặc định (vd: đạn bắn trúng)
    }
    
    //hello
}
