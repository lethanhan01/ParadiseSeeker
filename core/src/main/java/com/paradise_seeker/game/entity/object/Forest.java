package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class Forest extends GameObject {
    public Forest(float x, float y) {
        super(x, y, 4, 4, "images/objects/forest.png");
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.setSpeedMultiplier(0.8f); // Giảm tốc độ còn 50%
    }
}
