package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class LavaLake extends GameObject {
    public LavaLake(float x, float y) {
        super(x, y, 4, 4, "images/objects/lava_lake.png");
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.blockMovement(); // Ngăn chặn di chuyển
        player.takeDamage(1); // Mất 5 máu mỗi frame
    }
}
