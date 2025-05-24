package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class HPitem extends Item {
    private int healAmount;

    public HPitem(float x, float y, float size, String texturePath, int healAmount) {
        super(x, y, size, texturePath);
        this.healAmount = healAmount;
        this.name = "Health Potion";
        this.description = "Restores " + healAmount + " HP.";
    }

    @Override
    public void onCollision(Player player) {
        if (active) {
            player.addItemToInventory(this);
            active = false;
        }
    }
    public void use(Player player) {
        player.hp = Math.min(Player.MAX_HP, player.hp + healAmount);
        count--;
    }
}