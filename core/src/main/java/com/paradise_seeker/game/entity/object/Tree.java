package com.paradise_seeker.game.entity.object;

import com.paradise_seeker.game.entity.Player;

public class Tree extends GameObject {
    public Tree(float x, float y) {
        super(x, y, 2, 1, "images/objects/tree.png");
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.blockMovement();
    }
}
