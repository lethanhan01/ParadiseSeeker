package com.paradise_seeker.game.entity.skill;

import com.paradise_seeker.game.entity.Character;
import com.badlogic.gdx.math.MathUtils;

public class NyxSkill implements Skill {
    private int manaCost = 15;
    private long cooldown = 1800;
    private long lastUsedTime = 0;

    @Override
    public boolean canUse(long now) {
        return (now - lastUsedTime) >= cooldown;
    }

    @Override
    public void execute(Character target) {
        if (canUse(System.currentTimeMillis())) {
            castSkill(25, target); // dịch chuyển gần target
        }
    }

    @Override
    public void update(long now) {
        // Có thể thêm hiệu ứng mờ/ánh sáng tối
    }

    @Override
    public void castSkill(int atk, Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            System.out.println("Nyx dịch chuyển đến gần mục tiêu " + target);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void castSkill(int atk, int x, int y) {
        if (canUse(System.currentTimeMillis())) {
            // Triệu hồi ảo ảnh tại vị trí xung quanh (x, y)
            for (int i = 0; i < 3; i++) {
                int offsetX = MathUtils.random(-40, 40);
                int offsetY = MathUtils.random(-40, 40);
                System.out.println("Nyx triệu hồi ảo ảnh tại (" + (x + offsetX) + ", " + (y + offsetY) + ")");
            }
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }
} 
