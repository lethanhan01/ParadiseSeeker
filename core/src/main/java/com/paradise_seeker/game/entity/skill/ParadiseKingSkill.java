package com.paradise_seeker.game.entity.skill;

import com.paradise_seeker.game.entity.Character;
import com.badlogic.gdx.math.MathUtils;

public class ParadiseKingSkill implements Skill {
    private int manaCost = 30;
    private long cooldown = 3500;
    private long lastUsedTime = 0;
    private boolean transformed = false;

    @Override
    public boolean canUse(long now) {
        return (now - lastUsedTime) >= cooldown;
    }

    @Override
    public void execute(Character target) {
        if (!transformed && canUse(System.currentTimeMillis())) {
            castSkill(35, target); // Giai đoạn biến hình
        }
    }

    @Override
    public void update(long now) {
        // Có thể thêm hiệu ứng glow vàng nếu đã biến hình
    }

    @Override
    public void castSkill(int atk, Character target) {
        if (!transformed && canUse(System.currentTimeMillis())) {
            transformed = true;
            System.out.println("Paradise King biến hình! Sức mạnh tăng lên vĩnh viễn.");
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void castSkill(int atk, int x, int y) {
        if (canUse(System.currentTimeMillis())) {
            // Tấn công diện rộng: tạo vòng phát nổ ngẫu nhiên quanh tâm (x, y)
            for (int i = 0; i < 5; i++) {
                int offsetX = MathUtils.random(-60, 60);
                int offsetY = MathUtils.random(-60, 60);
                System.out.println("Paradise King gây nổ tại (" + (x + offsetX) + ", " + (y + offsetY) + ")");
            }
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }
} 
