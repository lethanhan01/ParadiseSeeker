package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.main.GameScreen;

public class PlayerSkill implements Skill {
    private int manaCost;
    private long cooldown;
    private long lastUsedTime;

    public PlayerSkill() {
        this.manaCost = 10;
        this.cooldown = 1500;
        this.lastUsedTime = 0;
    }

    @Override
    public boolean canUse(long now) {
        return (now - lastUsedTime) >= cooldown;
    }

    @Override
    public void execute(Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            target.receiveDamage(20);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void update(long now) {
        // Có thể cập nhật hiệu ứng hoặc thời gian còn lại nếu cần
    }

    @Override
    public void castSkill(int atk, Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            int damage = atk * 2;
            target.receiveDamage(damage);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    // Phương thức mới hỗ trợ hướng
    public void castSkill(int atk, int x, int y, String direction) {
        if (canUse(System.currentTimeMillis())) {
            LaserBeam laser = new LaserBeam(x, y, atk, direction);
            GameScreen.activeProjectiles.add(laser);
            lastUsedTime = System.currentTimeMillis();
            System.out.println("Người chơi đã tung kỹ năng LaserBeam về hướng: " + direction);
        }
    }

    @Override
    public void castSkill(int atk, int x, int y) {
        // Không còn dùng đến nếu muốn bắn theo hướng cụ thể
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public long getCooldown() {
        return cooldown;
    }

    public long getLastUsedTime() {
        return lastUsedTime;
    }
} 
