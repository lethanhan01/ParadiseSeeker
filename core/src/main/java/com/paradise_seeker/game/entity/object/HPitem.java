package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class HPitem extends Item {
    private int healAmount;

    public HPitem(float x, float y, float size, String texturePath, int healAmount) {
        super(x, y, size, texturePath);
        this.healAmount = healAmount;
        this.name = "Health Potion";
        this.description = "Restores " + healAmount + " HP.";
        this.stackable = true;
        this.maxStackSize = 5; // Giới hạn số lượng tối đa trong một stack
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
    public boolean canStackWith(Item other) {
        if (!(other instanceof HPitem)) return false;
        HPitem otherHP = (HPitem) other;
        return super.canStackWith(other) && this.healAmount == otherHP.healAmount;
    }
    public void dispose() {
		if (texture != null) {
			texture.dispose();
		}
	}
}