package com.paradise_seeker.game.entity.object.item;

import com.paradise_seeker.game.entity.Player;

public class MPitem extends Item {
    private int manaAmount;

    public MPitem(float x, float y, float size, String texturePath, int manaAmount) {
        super(x, y, size, texturePath);
        this.manaAmount = manaAmount;
        this.name = "Mana Potion";
        this.description = "Restores " + manaAmount + " MP.";
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
		player.mp = Math.min(Player.MAX_MP, player.mp + manaAmount);

	}
	public boolean canStackWith(Item other) {
		if (!(other instanceof MPitem)) return false;
		MPitem otherMP = (MPitem) other;
		return super.canStackWith(other) && this.manaAmount == otherMP.manaAmount;
	}
	public void dispose() {
		if (texture != null) {
			texture.dispose();
		}
	}
}