package com.paradise_seeker.game.entity.skill;
import com.paradise_seeker.game.entity.Character;

public class PlayerSkill1 implements Skill {
	private int manaCost;
	private long cooldown;
	private long lastUsedTime;
	public PlayerSkill1(int manaCost, long cooldown) {
		this.manaCost = manaCost;
		this.cooldown = cooldown;
		this.lastUsedTime = 0;
	}

	@Override
	public boolean canUse(long now) {
		return (now - lastUsedTime) >= cooldown;
	}

	@Override
	public void execute(Character target) {
		if (canUse(System.currentTimeMillis())) {
			// Logic to execute the skill
			lastUsedTime = System.currentTimeMillis();
		}
	}

	@Override
	public void update(long now) {
		// Update skill state if needed
	}

	@Override
	public void castSkill(int atk, Character target) {
		// Logic to cast skill on a target character
	}

	@Override
	public void castSkill(int atk, int x, int y) {
		// Logic to cast skill at specific coordinates
	}

	@Override
	public int getManaCost() {
		return manaCost;
	}


}
