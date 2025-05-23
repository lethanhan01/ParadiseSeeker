package com.paradise_seeker.game.entity.monster.boss;

public class TitanKing extends Boss {
    public TitanKing(float x, float y) {
        super(x, y, 100, 1.5f, 20); // hp, speed, cleaveDamage
    }

    @Override
    protected void loadAnimations() {
        walkRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/walk/phai/", "demon_walk_", 12);
        walkLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/walk/trai/", "demon_walk_", 12);

        idleRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/idle/phai/", "demon_idle_1 (", 6, ").png", 0);
        idleLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/idle/trai/", "demon_idle_", 6, ".png");

        cleaveRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/cleave/phai/", "demon_cleave_", 15, ".png");
        cleaveLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/cleave/trai/", "demon_cleave_", 15, ".png");

        takeHitRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/take_hit/phai/", "demon_take_hit_", 5);
        takeHitLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/take_hit/trai/", "demon_take_hit_", 5);

        deathRight = loadAnimation("images/Entity/characters/monsters/boss/boss_1/death/phai/", "demon_death_", 22, ".png");
        deathLeft = loadAnimation("images/Entity/characters/monsters/boss/boss_1/death/trai/", "demon_death_", 22, ".png");
    }
}
