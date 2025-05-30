package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class Fragment extends Item {
	private int fragmentIndex;

	public Fragment(float x, float y, float size, String texturePath, int fragmentIndex) {
		super(x, y, size, texturePath);
		this.fragmentIndex = fragmentIndex;
		this.name = "Fragment" + fragmentIndex;
		this.description = "A piece of a larger item.";
	}

	@Override

	public void use(Player player) {
		// Logic sử dụng Fragment nếu cần
	}


	@Override
	public void onCollision(Player player) {
		// TODO Auto-generated method stub
		
	}
}