package com.paradise_seeker.game.collision;

import com.paradise_seeker.game.entity.Player;

import java.util.List;

public class CollisionSystem {

    public static void checkCollisions(Player player, List<Collidable> collidables) {
        for (Collidable c : collidables) {
            if (player.getBounds().overlaps(c.getBounds())) {
                c.onCollision(player);
            }
        }
    }
}
