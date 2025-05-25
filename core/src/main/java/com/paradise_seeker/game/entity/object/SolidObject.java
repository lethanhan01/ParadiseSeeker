package com.paradise_seeker.game.entity.object;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.Player;


public class SolidObject implements Collidable {
    private Rectangle bounds;

    public SolidObject(Rectangle bounds) {
        this.bounds = bounds;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(Player player) {
        // No-op: Collision logic handled during movement, not here.

    }
}
