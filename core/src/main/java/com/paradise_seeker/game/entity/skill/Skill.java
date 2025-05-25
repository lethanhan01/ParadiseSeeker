package com.paradise_seeker.game.entity.skill;

import com.paradise_seeker.game.entity.Character;
import com.badlogic.gdx.math.Rectangle;

public interface Skill {
    boolean canUse(long now);
    void execute(Character user);
    void update(long now);
    void castSkill(int atk, Character target);
    void castSkill(int atk, int x, int y);  // Để bắn ở vị trí x, y
    void castSkill(int atk, int x, int y, String direction);  // Bắn theo hướng
    void castSkill(int atk, Rectangle playerBounds, String direction); // Bắn theo hướng từ player

    int getManaCost();
}
