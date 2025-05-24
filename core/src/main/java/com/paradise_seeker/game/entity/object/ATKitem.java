package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class ATKitem extends Item {
    private int atkBoost;

    public ATKitem(float x, float y, float size, String texturePath, int atkBoost) {
        super(x, y, size, texturePath);
        this.atkBoost = atkBoost;
        this.name = "Attack Boost";
        this.description = "Attack + " + atkBoost + ".";
    }

    @Override
    public void onCollision(Player player) {
        if (active) {
            player.addItemToInventory(this);
            active = false;
        }
    }
    public void use(Player player) {
		player.atk += atkBoost;
		count--;
	}
}