package com.paradise_seeker.game.entity.object;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.Player;

// A simple solid object for collision boundaries from Tiled map objects
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
        // Usually, you can leave this empty for static solids,
        // but you can add logic if you want special behavior on touch
    }
}
