package com.paradise_seeker.game.entity;

public interface Skill {
    boolean canUse(long now);
    void    execute(Character user);
    void    update(long now);
    //hello
}
