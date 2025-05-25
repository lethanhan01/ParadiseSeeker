package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class ATKitem extends Item {
    private int atkBoost;

    public ATKitem(float x, float y, float size, String texturePath, int atkBoost) {
        super(x, y, size, texturePath);
        this.atkBoost = atkBoost;
        this.name = "Attack Boost";
        this.description = "Attack + " + atkBoost + ".";
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
		player.atk += atkBoost;
		count--;
	}

	public boolean canStackWith(Item other) {
		if (!(other instanceof ATKitem)) return false;
		ATKitem otherATK = (ATKitem) other;
		return super.canStackWith(other) && this.atkBoost == otherATK.atkBoost;
	}

	public void dispose() {
		if (texture != null) {
			texture.dispose();
		}
	}
}