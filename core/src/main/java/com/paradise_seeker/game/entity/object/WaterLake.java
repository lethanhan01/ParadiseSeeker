package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class WaterLake extends GameObject {
    public WaterLake(float x, float y) {
        super(x, y, 4, 4, "images/objects/water_lake.png");
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.blockMovement(); // Ngăn chặn di chuyển
    }
}
