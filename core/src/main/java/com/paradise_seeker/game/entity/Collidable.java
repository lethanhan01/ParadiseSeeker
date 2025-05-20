package com.paradise_seeker.game.entity;

import com.badlogic.gdx.math.Rectangle;

public interface Collidable {
    
	Rectangle getBounds();
    void onCollision(Player player);
    
}
