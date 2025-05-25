package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class Skill1item extends Item {
    private float damageMultiplier;

    public Skill1item(float x, float y, float size, String texturePath) {
        super(x, y, size, texturePath);
        this.damageMultiplier = 0.5f;
        this.name = "Skill 1 Item";
        this.description = "Skill 1's DMG +0.5x.";
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
    	player.playerSkill1.setDamageMultiplier(this.damageMultiplier); // Nhân 2 damage skill1
    	count--;
	}


    public boolean canStackWith(Item other) {
		if (!(other instanceof Skill1item)) return false;
		Skill1item otherSkill = (Skill1item) other;
		return super.canStackWith(other) && this.name.equals(otherSkill.name);
	}

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
