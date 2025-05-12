package com.paradise_seeker.game.entity.skill;

import com.paradise_seeker.game.entity.Character;

public interface Skill {
    boolean canUse(long now);
    void    execute(Character user);
    void    update(long now);
    void 	castSkill(int atk, Character target);
    void 	castSkill(int atk, int x, int y);
    int  getManaCost();
}
