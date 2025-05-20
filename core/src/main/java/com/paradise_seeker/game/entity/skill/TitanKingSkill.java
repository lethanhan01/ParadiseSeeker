package com.paradise_seeker.game.entity.skill;

import com.paradise_seeker.game.entity.Character;
import com.badlogic.gdx.math.MathUtils;

public class TitanKingSkill implements Skill {
    private int manaCost = 25;
    private long cooldown = 2500;
    private long lastUsedTime = 0;

    @Override
    public boolean canUse(long now) {
        return (now - lastUsedTime) >= cooldown;
    }

    @Override
    public void execute(Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            castSkill(28, target);
        }
    }

    @Override
    public void update(long now) {
        // Có thể tạo hiệu ứng rung màn hình
    }

    @Override
    public void castSkill(int atk, Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            int damage = atk * 2;
            target.receiveDamage(damage);
            System.out.println("Titan King đập đất gây sát thương trực tiếp cho mục tiêu: " + damage);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void castSkill(int atk, int x, int y) {
        if (canUse(System.currentTimeMillis())) {
            // Đập đất tạo chấn động vùng xung quanh
            for (int i = 0; i < 4; i++) {
                int offsetX = MathUtils.random(-50, 50);
                int offsetY = MathUtils.random(-50, 50);
                System.out.println("Titan King tạo dư chấn tại (" + (x + offsetX) + ", " + (y + offsetY) + ")");
            }
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }
} 