package com.paradise_seeker.game.entity.object.item;

import com.paradise_seeker.game.entity.Player;

public class Fragment extends Item {
	private int fragmentIndex;
	private boolean isCollected = false;

	public Fragment(float x, float y, float size, String texturePath, int fragmentIndex) {
		super(x, y, size, texturePath);
		this.fragmentIndex = fragmentIndex;
		this.name = "Fragment" + fragmentIndex;
		this.description = "A piece of a larger item.";
		if (fragmentIndex == 4) {
			this.name = "Fragment of the Lost Treasure";
			this.description = "The key to a final battle.";
		}
	}

	@Override

	public void use(Player player) {
		if (fragmentIndex == 4) {
			
		}
	}
	
	public int getFragmentIndex() {
		return fragmentIndex;
	}

	@Override
	public void onCollision(Player player) {
		// TODO Auto-generated method stub
		
	}
}