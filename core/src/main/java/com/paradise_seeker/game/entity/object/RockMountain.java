package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class RockMountain extends GameObject {
    public RockMountain(float x, float y) {
        super(x, y, 4, 4, "images/objects/rock_mountain.png");
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.setClimbing(true); // ✅ Cho phép đi xuyên qua, nhưng bật trạng thái leo
    }
}
