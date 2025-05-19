package com.paradise_seeker.game.entity.skill;

import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.main.GameScreen;
import com.badlogic.gdx.math.MathUtils;

public class DragonKingSkill implements Skill {
    private int manaCost = 20;
    private long cooldown = 2000;
    private long lastUsedTime = 0;

    @Override
    public boolean canUse(long now) {
        return (now - lastUsedTime) >= cooldown;
    }

    @Override
    public void execute(Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            castSkill(30, target);
        }
    }

    @Override
    public void update(long now) {
        // Không cần update gì đặc biệt hiện tại
    }

    @Override
    public void castSkill(int atk, Character target) {
        if (canUse(System.currentTimeMillis()) && target != null) {
            int damage = atk * 3;
            target.receiveDamage(damage);
            System.out.println("Dragon King lao vào mục tiêu và gây sát thương " + damage);
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void castSkill(int atk, int x, int y) {
        if (canUse(System.currentTimeMillis())) {
            // Phun lửa: tạo nhiều điểm sát thương dạng quạt trước mặt
            int numFlames = 5;
            int spread = 80; // góc lan
            int baseAngle = 0; // hướng chính diện (có thể thay bằng hướng boss nếu cần)

            for (int i = 0; i < numFlames; i++) {
                int angle = baseAngle - spread / 2 + (spread / (numFlames - 1)) * i;
                float offsetX = MathUtils.cosDeg(angle) * 40;
                float offsetY = MathUtils.sinDeg(angle) * 40;
                System.out.println("Dragon King phun lửa tại (" + (x + offsetX) + ", " + (y + offsetY) + ")");
                // Có thể tạo đối tượng FlameEffect tại vị trí này nếu bạn có
            }
            lastUsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }
} 
